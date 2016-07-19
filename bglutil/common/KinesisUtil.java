package bglutil.common;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import bglutil.common.kinesis.KCLRecordsPrinterFactory;
import bglutil.main.Biu;

import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.services.kinesis.model.PutRecordsResultEntry;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import com.amazonaws.services.kinesis.producer.UserRecordResult;

public class KinesisUtil {
	
	public void printAllPhysicalId(AmazonKinesis k){
		for(String streamName:k.listStreams().getStreamNames()){
			System.out.println("kinesis: "+streamName);
		}
	}

	/**
	 * For testing, calling Producer.
	 * @param streamName
	 * @param parallelDegree
	 * @param recordsPerPut
	 */
	public void produceRandomRecords(String streamName, int parallelDegree, int recordsPerPut, String profile){
		Producer[] workers = new Producer[parallelDegree];
		for(int i=0;i<parallelDegree;i++){
			workers[i] = new Producer(streamName,recordsPerPut,i,profile);
			workers[i].start();
		}
		try {
			Thread.sleep(30*1000);
			workers[0].wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void produceRandomRecordsByKPL(String streamName, int parallelDegree, int userRecordsToAggregate, int recordsPerPut, String profile, String tempDir){
		Region region = Region.getRegion(Biu.PROFILE_REGIONS.get(profile));
		KinesisProducerConfiguration config = new KinesisProducerConfiguration()
							.setAggregationEnabled(true)
							.setAggregationMaxCount(userRecordsToAggregate)
							.setCollectionMaxCount(recordsPerPut)
        					.setRecordMaxBufferedTime(3000)
        					.setMaxConnections(parallelDegree)
        					.setMinConnections(parallelDegree)
        					.setRequestTimeout(60000)
        					.setCustomEndpoint(region.getServiceEndpoint(AmazonKinesis.ENDPOINT_PREFIX))
        					.setMetricsNamespace("dropme-app-kpl")
        					.setTempDirectory(tempDir)
        					.setCredentialsProvider(AccessKeys.getCredentialsByProfile(profile))
        					.setRegion(region.getName());
		KinesisProducer kp = new KinesisProducer(config);
		Random r = new Random(1);
		int partKey = 0;
		String data = null;
		try {
			while(true){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				partKey = r.nextInt(10000);
				data = (new Date().toString());
				kp.addUserRecord(streamName, 
							Integer.toString(partKey), 
							ByteBuffer.wrap(data.getBytes("UTF-8")));
				System.out.println("KPL => PartKey: "+partKey+", User Data: "+data);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void consumeRandomRecordsByKCL(String streamName, InitialPositionInStream initialPositionInStream, String initialStyle, String profile) throws Exception{
		System.out.println("Profile: "+profile);
		String style = initialStyle.equals("new")?"new":"join";
		System.out.println("Application worker instance initialization style: "+style);
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByProfile(Clients.DDB, profile);
		String appName = streamName+"-app-kcl";
		DynamoDBUtil ddbUtil = new DynamoDBUtil();
		try{
			String tableName = ddb.describeTable(appName).getTable().getTableName();
			System.out.println("DDB table name: "+tableName);
			if(style.equals("new")){
				ddbUtil.deleteTable(ddb, appName);
				Thread.sleep(1000*30);
			}
		}catch (ResourceNotFoundException ex){
			System.out.println("DDB table "+appName+" not exist");
		}
		String workerId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
		System.out.println("Worker named: "+workerId);
		KinesisClientLibConfiguration kcc = new KinesisClientLibConfiguration(appName, streamName, AccessKeys.getCredentialsByProfile(profile), workerId);		
		kcc.withInitialPositionInStream(initialPositionInStream);
		kcc.withRegionName(Biu.PROFILE_REGIONS.get(profile).getName());
		kcc.withIdleTimeBetweenReadsInMillis(500);
		
        Worker worker = new Worker.Builder()
        	.recordProcessorFactory(new KCLRecordsPrinterFactory())
        	.config(kcc)
        	.build();
        int exitCode = 0;
        System.out.println("Try to start worker.");
        try {
            worker.run();
            System.out.println("Worker "+workerId+" started.");
        } catch (Throwable t) {
            System.err.println("Caught throwable while processing data.");
            t.printStackTrace();
            exitCode = 1;
        }
        System.exit(exitCode);
	}

	public KinesisClientLibConfiguration getDefaultKCLConfiguration(){
		return new KinesisClientLibConfiguration("dummy", "dummy", AccessKeys.getCredentialsByProfile("beijing"), "dummy");		
	}
}

class Producer extends Thread{
	private String streamName;
	private int recordsPerPut;
	private String profile;
	private int seed;
	
	public Producer(String streamName, int recordsPerPut, int seed, String profile){
		this.streamName = streamName;
		this.recordsPerPut = recordsPerPut;
		this.seed = seed;
		this.profile = profile;
	}
	
	public void run(){
		try{
			AmazonKinesis k = (AmazonKinesis) Clients.getClientByProfile(Clients.KINESIS, this.profile);
			PutRecordsRequest prr = new PutRecordsRequest()
										.withStreamName(this.streamName);
			List<PutRecordsRequestEntry> putRecordsRequestEntries = null;
			PutRecordsRequestEntry entry = null;
			Random r = new Random(this.seed);
			PutRecordsResult prrr = null;
			String[] payloads = new String[this.recordsPerPut];
			int partKey = 0;
			while(true){
				putRecordsRequestEntries = new ArrayList<PutRecordsRequestEntry>();
				String payload = null;
				for(int i=0;i<this.recordsPerPut;i++){
					entry = new PutRecordsRequestEntry();
					partKey = r.nextInt(10000);			// partition key
					payload = (new Date()).toString();	// data payload
					entry.setData(ByteBuffer.wrap(String.valueOf(payload).getBytes("UTF-8")));
					payloads[i] = payload;
					entry.setPartitionKey(Integer.toString(partKey));
					putRecordsRequestEntries.add(entry);
				}
				prr.setRecords(putRecordsRequestEntries);
				prrr = k.putRecords(prr);
				try {
					Thread.sleep(1*500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//System.out.printf("[%s] as %s\n",payload,prrr);
				int c=0;
				for(PutRecordsResultEntry e: prrr.getRecords()){
					System.out.println("P => Data: "+payloads[c]+" to "+e.getShardId()+" with seg#: "+e.getSequenceNumber());
					c++;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
