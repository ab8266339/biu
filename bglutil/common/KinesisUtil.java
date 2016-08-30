package bglutil.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import bglutil.common.kinesis.KCLRecordsPrinterFactory;

import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.model.DeleteStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamRequest;
import com.amazonaws.services.kinesis.model.MergeShardsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.services.kinesis.model.PutRecordsResultEntry;
import com.amazonaws.services.kinesis.model.Shard;
import com.amazonaws.services.kinesis.model.SplitShardRequest;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;

public class KinesisUtil implements IUtil{
	
	public void printAllPhysicalId(Object k){
		for(String streamName:((AmazonKinesis)k).listStreams().getStreamNames()){
			System.out.println("kinesis: "+streamName);
		}
	}
	
	public void deleteStream(AmazonKinesis k, String streamName){
		k.deleteStream(new DeleteStreamRequest().withStreamName(streamName));
		System.out.println("=> Deleting stream "+streamName);
	}
	
	public void printShardInStream(AmazonKinesis k, String streamName){
		ArrayList<String> parentShardIds = new ArrayList<String>();
		TreeMap<BigDecimal,Shard> openShards = new TreeMap<BigDecimal,Shard>();
		for (Shard shard:k.describeStream(streamName).getStreamDescription().getShards()){
			if(shard.getParentShardId()!=null){
				parentShardIds.add(shard.getParentShardId());
			}
			if(shard.getAdjacentParentShardId()!=null){
				parentShardIds.add(shard.getAdjacentParentShardId());
			}
		}
		System.out.println();
		for (Shard shard:k.describeStream(streamName).getStreamDescription().getShards()){
			if(parentShardIds.contains(shard.getShardId())){
				System.out.println("Closed: "+shard.getShardId()+"\n");
			}
			else{
				openShards.put(new BigDecimal(shard.getHashKeyRange().getStartingHashKey()),shard);
			}
		}
		for(BigDecimal startingHashKey:openShards.keySet()){
			System.out.println("Open: "+openShards.get(startingHashKey).getShardId()+": \n\tsHash:"+startingHashKey+"\n\teHash:"+openShards.get(startingHashKey).getHashKeyRange().getEndingHashKey()+"\n");
		}
	}
	
	private TreeMap<BigDecimal,Shard> getOpenShards(AmazonKinesis k, String streamName){
		ArrayList<String> parentShardIds = new ArrayList<String>();
		TreeMap<BigDecimal,Shard> openShards = new TreeMap<BigDecimal,Shard>();
		for (Shard shard:k.describeStream(streamName).getStreamDescription().getShards()){
			if(shard.getParentShardId()!=null){
				parentShardIds.add(shard.getParentShardId());
			}
			if(shard.getAdjacentParentShardId()!=null){
				parentShardIds.add(shard.getAdjacentParentShardId());
			}
		}
		for (Shard shard:k.describeStream(streamName).getStreamDescription().getShards()){
			if(!parentShardIds.contains(shard.getShardId())){
				openShards.put(new BigDecimal(shard.getHashKeyRange().getStartingHashKey()),shard);
			}
		}
		return openShards;	
	}
	
	public void mergeShards(AmazonKinesis k, String streamName, String shard1, String shard2){
		k.mergeShards(new MergeShardsRequest()
						.withStreamName(streamName)
						.withShardToMerge(shard1).withAdjacentShardToMerge(shard2));
	}
	
	public void streamCollapse(AmazonKinesis k, String streamName){
		TreeMap<BigDecimal,Shard> openShards = this.getOpenShards(k, streamName);
		if(openShards.size() % 2 !=0){
			System.out.println("openShards.size() % 2 !=0, operations aborted.");
		}
		else{
			int c=0;
			String[] shards = new String[2];
			for(BigDecimal startingHashKey:openShards.keySet()){
				shards[c] = openShards.get(startingHashKey).getShardId();
				if(c==1){
					System.out.println("Merging "+shards[0]+" and "+shards[1]);
					this.mergeShards(k, streamName, shards[0], shards[1]);	
				}
				c++;
				c=(c==2)?0:c;
			}
		}
	}
	
	public void streamFission(AmazonKinesis k, String streamName){
		TreeMap<BigDecimal,Shard> openShards = this.getOpenShards(k, streamName);
		for(BigDecimal startingHashKey:openShards.keySet()){
			this.splitShardInHalf(k, streamName, openShards.get(startingHashKey).getShardId());
		}
	}
	
	public void splitShardInHalf(AmazonKinesis k, String streamName, String shardToSplit){
		SplitShardRequest ssr = new SplitShardRequest()
												.withStreamName(streamName)
												.withShardToSplit(shardToSplit);
		DescribeStreamRequest dsr = new DescribeStreamRequest()
												.withStreamName(streamName);
		List<Shard> shards = k.describeStream(dsr).getStreamDescription().getShards();
		System.out.println("=> before:");
		boolean found = false;
		for(Shard s:shards){
			if(s.getShardId().equals(shardToSplit)){
				found = true;
				System.out.println(s.getShardId()+": "+s.getHashKeyRange().getStartingHashKey()+","+s.getHashKeyRange().getEndingHashKey());
				System.out.println("Splitting "+s.getShardId());
				BigInteger startHashKey = new BigInteger(s.getHashKeyRange().getStartingHashKey());
				BigInteger endHashKey = new BigInteger(s.getHashKeyRange().getEndingHashKey());
				String newStartingHashKey  = startHashKey.add(endHashKey).divide(new BigInteger("2")).toString();
				ssr.setNewStartingHashKey(newStartingHashKey);
				k.splitShard(ssr);
				break;
			}
		}
		if(found){
		try {
			Thread.sleep(30*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		else{
			System.out.println(shardToSplit+" not found.");
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
		Region region = Clients.getRegion(profile);
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
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		String appName = streamName+"-app-kcl";
		DynamodbUtil ddbUtil = new DynamodbUtil();
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
		kcc.withRegionName(GeneralUtil.PROFILE_REGIONS.get(profile).getName());
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
			AmazonKinesis k = (AmazonKinesis) Clients.getClientByServiceClassProfile(Clients.KINESIS, this.profile);
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
					entry.setData(ByteBuffer.wrap(String.valueOf(payload).getBytes("UTF-8"))); // bytes stream
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
