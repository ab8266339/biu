package bglutil.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.http.conn.ConnectTimeoutException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import bglutil.common.AutoscalingUtil;
import bglutil.common.CloudFormationUtil;
import bglutil.common.CloudFrontUtil;
import bglutil.common.CloudWatchUtil;
import bglutil.common.Clients;
import bglutil.common.CloudSearchUtil;
import bglutil.common.CloudTrailUtil;
import bglutil.common.CodeCommitUtil;
import bglutil.common.CodeDeployUtil;
import bglutil.common.ConfigUtil;
import bglutil.common.DynamodbUtil;
import bglutil.common.EC2Util;
import bglutil.common.ElasticLoadbalancingUtil;
import bglutil.common.ElasticMapReduceUtil;
import bglutil.common.ElasticacheUtil;
import bglutil.common.GeneralUtil;
import bglutil.common.GlacierUtil;
import bglutil.common.CloudHSMUtil;
import bglutil.common.Helper;
import bglutil.common.IAMUtil;
import bglutil.common.KeyManagementServiceUtil;
import bglutil.common.KinesisUtil;
import bglutil.common.LambdaUtil;
import bglutil.common.CloudWatchLogsUtil;
import bglutil.common.RDSUtil;
import bglutil.common.S3Util;
import bglutil.common.SNSUtil;
import bglutil.common.SQSUtil;
import bglutil.common.STSUtil;
import bglutil.common.types.GLTreeMap;
import bglutil.conf.Config;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.ServiceAbbreviations;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.Stack;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudhsm.AWSCloudHSM;
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearch;
import com.amazonaws.services.cloudtrail.AWSCloudTrail;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.codecommit.AWSCodeCommit;
import com.amazonaws.services.codedeploy.AmazonCodeDeploy;
import com.amazonaws.services.config.AmazonConfig;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.AttachVolumeResult;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.DeleteRouteTableRequest;
import com.amazonaws.services.ec2.model.DeleteVolumeRequest;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeRouteTablesRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSubnetsRequest;
import com.amazonaws.services.ec2.model.DescribeSubnetsResult;
import com.amazonaws.services.ec2.model.DescribeVolumesRequest;
import com.amazonaws.services.ec2.model.DetachVolumeRequest;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceBlockDeviceMapping;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RouteTable;
import com.amazonaws.services.ec2.model.Volume;
import com.amazonaws.services.ec2.model.VolumeState;
import com.amazonaws.services.ec2.model.VolumeType;
import com.amazonaws.services.elasticache.AmazonElastiCache;
import com.amazonaws.services.elasticache.model.CacheCluster;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.model.ClusterSummary;
import com.amazonaws.services.elasticmapreduce.model.InstanceState;
import com.amazonaws.services.glacier.AmazonGlacier;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.Group;
import com.amazonaws.services.identitymanagement.model.InstanceProfile;
import com.amazonaws.services.identitymanagement.model.Role;
import com.amazonaws.services.identitymanagement.model.User;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DBSnapshot;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityResult;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;

/**
 * Biu!!!
 * @author guanglei
 */
public class Biu {
	
	private static final Helper h = new Helper();
	
	// Deprecated. Retained for showGap().
	public static final TreeMap<String,String> SERVICEABB_SERVICENAME = new TreeMap<String,String>();
	
	// The public methods that will not be dynamically invoked.
	public static final ArrayList<String> SKIPPED_METHODS = new ArrayList<String>();
	
	static {
		// TTL preferences.
		java.security.Security.setProperty("networkaddress.cache.ttl", "30");
		
		// The source of truth about available services.
		Class<?> clazz = ServiceAbbreviations.class;
		Field[] fields = clazz.getDeclaredFields();
		for(Field f:fields){
			try {
				SERVICEABB_SERVICENAME.put(f.get(null).toString(),f.getName());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		// The methods ignored by dynamic caller.
		SKIPPED_METHODS.add("main");
		SKIPPED_METHODS.add("coreV2");
	}

	/**
	 * Endless connect to specified address with or without http:// URI prefix.
	 * @param address
	 * @throws Exception
	 */
	public void urlTest(String address) throws Exception{
		h.help(address,"<url-for-testing>");
		if(!address.startsWith("http")){
			address="http://"+address;
		}
		ArrayList<URL> urls = new ArrayList<URL>();
		try {
			urls.add(new URL(address));
		} catch (MalformedURLException e) {
			// Will NOT happen.
			e.printStackTrace();
		}
		// Wait for port open.
		System.out.println(urls.get(0).toString());
		BufferedReader br;
		boolean dive = true;
		int count = 0;
		long start = 0L;
		long end = 0L;
		while (dive) {
			try {
				start = System.currentTimeMillis();
				br = new BufferedReader(new InputStreamReader(urls
						.get(0).openConnection().getInputStream()));
				String lineOne = br.readLine();
				end = System.currentTimeMillis();
				System.out.println(++count+": Reading first line: "+lineOne+" ("+(end-start)+"ms) OK.");
				br.close();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				try {
					Thread.sleep(7 * 1000);
				} catch (InterruptedException e) {
				}
			} finally{
				try {
					Thread.sleep(1 * 1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	/**
	 * The ARN generator. Returned ARN are true only in syntax.
	 * @param service
	 * @param resourceType
	 * @param resourceName
	 * @param discriminator
	 * @param profile
	 * @throws Exception
	 */
	public static void showArn(String service, String resourceType, String resourceName, String discriminator, String profile) throws Exception{
		h.help(service,"<service-name> <resource-type> <resource-name> <discriminator: null|session-id> <profile>");
		System.out.println("\n"+GeneralUtil.getArn(service, resourceType, resourceName, discriminator.equalsIgnoreCase("null")?null:discriminator, profile)+"\n");
	}
	
	/**
	 * Show names for all regions.
	 */
	public static void showRegionName(){
		System.out.println();
		System.out.println("Search: AWS Infrastructure | AWS全球基础设施\n");
		int i = 0;
		for(String r: GeneralUtil.REGIONCODE_REGIONNAME.keySet()){
			System.out.println("\t"+(++i)+": "+r+" <==> "+GeneralUtil.REGIONCODE_REGIONNAME.get(r)+((r.startsWith("us-gov-")||r.startsWith("cn-"))?"*":""));
		}
		System.out.println();
	}
	
	/**
	 * Gen SSO URL for federated user.
	 * @param federatedUser
	 * @throws Exception
	 */
	public void generateSsoUrlGlobal(String federatedUser) throws Exception{
		h.help(federatedUser,"<federatedUser>");
		STSUtil util = new STSUtil();
		AWSSecurityTokenService sts = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, "global");
		String url = util.getFederatedUserAwsConsoleSsoUrlForEc2S3AdminGlobal(sts, federatedUser, "http://aws.amazon.com");
		System.out.println(url);
	}
	
	public void generateSsoUrlChina(String federatedUser) throws Exception{
		h.help(federatedUser,"<federatedUser>");
		STSUtil util = new STSUtil();
		AWSSecurityTokenService sts = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, "china");
		String url = util.getFederatedUserAwsConsoleSsoUrlForEc2S3AdminChina(sts, federatedUser, "http://www.amazonaws.cn");
		System.out.println(url);
	}
	
	public void deleteArchive(String vaultName, String archiveId, String profile) throws Exception{
		h.help(vaultName,"<vault-name> <archive-id> <profile>");
		GlacierUtil util = new GlacierUtil();
		AmazonGlacier g = (AmazonGlacier) Clients.getClientByServiceClassProfile(Clients.GLACIER, profile);
		util.deleteArchive(g, vaultName, archiveId);
	}
	
	public void deleteArchiveByJobId(String vaultName, String jobId, String profile) throws Exception{
		h.help(vaultName,"<vault-name> <inventory-retrieval-job-id> <profile>");
		GlacierUtil util = new GlacierUtil();
		AmazonGlacier g = (AmazonGlacier) Clients.getClientByServiceClassProfile(Clients.GLACIER, profile);
		util.removeArchviesByRetrievalJobId(g, vaultName, jobId);
	}
	
	public void showGlacierJobOutput(String vaultName, String jobId, String profile) throws Exception{
		h.help(vaultName,"<vault-name> <job-id> <profile>");
		GlacierUtil util = new GlacierUtil();
		AmazonGlacier g = (AmazonGlacier) Clients.getClientByServiceClassProfile(Clients.GLACIER, profile);
		util.printGlacierJobOutput(g, vaultName, jobId);
	}
	
	public void retrieveVaultInventory(String vaultName, String topicName, String profile) throws Exception{
		h.help(vaultName,"<vault-name> <sns-topic-name> <profile>");
		GlacierUtil util = new GlacierUtil();
		AmazonGlacier g = (AmazonGlacier) Clients.getClientByServiceClassProfile(Clients.GLACIER, profile);
		String snsTopicArn = new SNSUtil().getTopicArn(topicName, profile);
		System.out.println(util.retrieveInventory(g, vaultName, snsTopicArn));
	}
	
	public void publish(String topicName, String subject, String body, String profile) throws Exception{
		h.help(topicName,"<topic-name> <subject> <body> <profile>");
		SNSUtil sns = new SNSUtil();
		sns.publish(topicName, subject, body, profile);
		System.out.println("Message published to "+sns.getTopicArn(topicName, profile));
	}
	
	public void clearOrphanSnapshot(String profile) throws Exception{
		h.help(profile,"<profile>");
		EC2Util util = new EC2Util();
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		AWSSecurityTokenService sts = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, profile);
		util.clearOrphanSnapshot(ec2, sts.getCallerIdentity(new GetCallerIdentityRequest()).getAccount());
	}
	
	/**
	 * Show instance health status for an ELB.
	 * @param elbName
	 * @param profile
	 * @throws Exception
	 */
	public void showInstanceHealthByElb(String elbName, String profile) throws Exception{
		h.help(elbName,"<elb-name> <profile>");
		ElasticLoadbalancingUtil util = new ElasticLoadbalancingUtil();
		AmazonElasticLoadBalancing elb = (AmazonElasticLoadBalancing) Clients.getClientByServiceClassProfile(Clients.ELB, profile);
		util.describeInstanceHealthByElbName(elb, elbName);
	}
	
	/**
	 * Copy source bucket to target bucket within region.
	 * @param regionPartition
	 * @param sourceBucketName
	 * @param destinationBucketName
	 * @throws Exception
	 */
	public void copyBucket(String sourceBucketName, String destinationBucketName, String profile) throws Exception{
		h.help(sourceBucketName,"<source-bucket> <destination-bucket> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		util.copyBucket(s3, sourceBucketName, destinationBucketName);
	}
	

	public void demoSqsOrder(String longPoll) throws Exception{
		h.help(longPoll,"<longPoll: true | false>");
		System.out.println("Sending 7 messages to "+Config.BEIJING_DEMO_SQS_QUEUE_URL);
		AmazonSQS sqs = (AmazonSQS) Clients.getClientByServiceClassProfile(Clients.SQS, "beijing");
		SendMessageRequest smr = new SendMessageRequest().withQueueUrl(Config.BEIJING_DEMO_SQS_QUEUE_URL);
		for(int i=1;i<8;i++){
			smr.setMessageBody("#"+i+" Test Message");
			System.out.println("Sending => "+"#"+i+" Test Message");
			sqs.sendMessage(smr);
		}
		System.out.println("Sending Done.");
		System.out.println("Try to receive message from "+Config.BEIJING_DEMO_SQS_QUEUE_URL);
		ReceiveMessageRequest rmr = new ReceiveMessageRequest().withQueueUrl(Config.BEIJING_DEMO_SQS_QUEUE_URL);
		rmr.setMaxNumberOfMessages(1); // 1~10 Returned messages can be fewer, it is NOT guaranteed.
		if(Boolean.valueOf(longPoll)){
			rmr.setWaitTimeSeconds(20);
		}
		List<Message> messages = null;
		ReceiveMessageResult rmrt = null;
		for(int i=1;i<8;i++){
			rmrt = sqs.receiveMessage(rmr);
			messages = rmrt.getMessages();
			String title = null;
			for (Message message : messages) {
			title = "Receiving => "+message.getBody();
				System.out.print(title);
				String receiptHandle = message.getReceiptHandle();
				sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(Config.BEIJING_DEMO_SQS_QUEUE_URL).withReceiptHandle(receiptHandle));
				System.out.println(", deleted");
			}
		}
		System.out.println("Receiving Done.");
	}
	
	public void demoDefaultKplConfig(){
		KinesisProducerConfiguration config = new KinesisProducerConfiguration();
		System.out.println();
		System.out.println("Aggregation Max Count: "+config.getAggregationMaxCount());
		System.out.println("Aggregation Max Size: "+config.getAggregationMaxSize());
		System.out.println("Collection Max Count: "+config.getCollectionMaxCount());
		System.out.println("Collection Max Size: "+config.getCollectionMaxSize());
		System.out.println("Log Level: "+config.getLogLevel());
		System.out.println("Max Connections: "+config.getMaxConnections());
		System.out.println("Metrics Granularity: "+config.getMetricsGranularity());
		System.out.println("Metrics Level: "+config.getMetricsLevel());
		System.out.println("Metrics Namespace: "+config.getMetricsNamespace());
		System.out.println("Metrics Upload Delay: "+config.getMetricsUploadDelay());
		System.out.println("Min Connections: "+config.getMinConnections());
		System.out.println("Port: "+config.getPort());
		System.out.println("Rate Limit: "+config.getRateLimit());
		System.out.println("Record Max Buffered Time: "+config.getRecordMaxBufferedTime());
		System.out.println("Record TTL: "+config.getRecordTtl());
		System.out.println("Region: "+config.getRegion());
		System.out.println("Request Timeout: "+config.getRequestTimeout());
		System.out.println("Temp Directory: "+config.getTempDirectory());
	}
	
	//TODO a demo to show whether pagination conflicts with consistent read.
	public void demoDdbPaginationAndConsistentRead() throws Exception{
		
	}
	
	public void demoDdbCondUpdate(String targetValue, String expectedValue) throws Exception{
		h.help(targetValue,"<targetValue> <expected-value>");
		String pseduoSQL = "\nupdate hash-table-hash-gsi\n  set name="+targetValue+"\n  where id=1\n  expecting gid="+expectedValue;
		System.out.println(pseduoSQL);
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, "beijing");
		DynamodbUtil util = new DynamodbUtil();
		System.out.println("\n Actual item:");
		util.getItemByPkHashString(ddb, "hash-table-hash-gsi", "id", "1", false);
		System.out.println();
		DynamoDB dynamoDB = new DynamoDB(ddb);
		UpdateItemSpec updateItemSpec = new UpdateItemSpec()
		.withPrimaryKey("id","1")
        .withUpdateExpression("set #name = :valNew")
        .withConditionExpression("#gid = :valOld")
        .withNameMap(new NameMap()
        	.with("#gid", "gid")
        	.with("#name", "name"))
        .withValueMap(new ValueMap()
        	.withString(":valNew", targetValue)
        	.withString(":valOld", expectedValue))
        .withReturnValues(ReturnValue.ALL_NEW);
		Table table = dynamoDB.getTable("hash-table-hash-gsi");
        UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
        System.out.println("UpdateItem Outcome:");
        System.out.println(outcome.getItem());
	}
	
	public void demoDdbQuery() throws Exception{
		String tableName = "stream-checker";
		AmazonDynamoDB ddb =  (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, "beijing");
		DynamoDB dynamoDB = new DynamoDB(ddb);
		QuerySpec querySpec = new QuerySpec()
		.withKeyConditionExpression("#hashCol = :keyColSearchValue AND #rangeCol >= :rangeColSearchVaule")
		.withFilterExpression("#ver <> :v")
		.withProjectionExpression("#target")
		.withNameMap(new NameMap()
						.with("#hashCol", "HashKey")
						.with("#rangeCol", "RangeKey")
						.with("#ver", "version")
						.with("#target", "LogicalObject"))
		.withValueMap(new ValueMap()
						.withString(":keyColSearchValue", "dropme:SimpleCheckpointer:1")
						.withString(":rangeColSearchVaule", "shard")
						.withNumber(":v", new BigDecimal(1)))
		.withReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);
		Table table = dynamoDB.getTable(tableName);
		ItemCollection<QueryOutcome> items = table.query(querySpec);
		System.out.println("Max Result Size: "+items.getMaxResultSize());
		System.out.println("Accumulated Scanned Count: "+items.getAccumulatedScannedCount());
		System.out.println("Accumulated Item Count: "+items.getAccumulatedItemCount());
		IteratorSupport<Item,QueryOutcome> iter = items.iterator();
		System.out.println("=> Result:");
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
		System.out.println("=> End");
		System.out.println("Accumulated Scanned Count: "+items.getAccumulatedScannedCount());
		System.out.println("Accumulated Item Count: "+items.getAccumulatedItemCount());
	}
	
	public void demoDefaultKclConfig() throws Exception{
		KinesisUtil util = new KinesisUtil();
		KinesisClientLibConfiguration kcc = util.getDefaultKCLConfiguration();
		System.out.println();
		System.out.println("Failover Time (ms): "+kcc.getFailoverTimeMillis());
		System.out.println("Idle Time between Reads (ms): "+kcc.getIdleTimeBetweenReadsInMillis());
		System.out.println("Initial Lease Table Read Capacity: "+kcc.getInitialLeaseTableReadCapacity());
		System.out.println("Initial Lease Table Write Capacity: "+kcc.getInitialLeaseTableWriteCapacity());
		System.out.println("Initial Position in Stream: "+kcc.getInitialPositionInStream().toString());
		System.out.println("Kinesis Endpoint: "+kcc.getKinesisEndpoint());
		System.out.println("Max Leases for Worker: "+kcc.getMaxLeasesForWorker());
		System.out.println("Max Leases to Steal at one Time: "+kcc.getMaxLeasesToStealAtOneTime());
		System.out.println("Max Records: "+kcc.getMaxRecords());
		System.out.println("Metrics Buffer Time (ms): "+kcc.getMetricsBufferTimeMillis());
		System.out.println("Matrics Level Name: "+kcc.getMetricsLevel().getName());
		System.out.println("Matrics Level Value: "+kcc.getMetricsLevel().getValue());
		System.out.println("Metrics Max Queue Size: "+kcc.getMetricsMaxQueueSize());
		System.out.println("Parent Shard Poll Interval (ms): "+kcc.getParentShardPollIntervalMillis());
		System.out.println("Region Name: "+kcc.getRegionName());
		System.out.println("Shard Sync Interval (ms): "+kcc.getShardSyncIntervalMillis());
		System.out.println("Task Backoff Time (ms): "+kcc.getTaskBackoffTimeMillis());
	}
	
	/**
	 * Generate/Encrypt/Decrypt data key.
	 * @param keyId
	 * @param profile
	 * @throws Exception
	 */
	public void demoKms(String keyId, String profile) throws Exception{
		h.help(keyId,"<key-id> <profile>");
		KeyManagementServiceUtil util = new KeyManagementServiceUtil();
		AWSKMSClient kms = (AWSKMSClient) Clients.getClientByServiceClassProfile(Clients.KMS, profile);
		util.generateDataKeyAndDecrypt(kms, keyId);
	}
	
	public void showCaller(String profile) throws Exception{
		h.help(profile,"<profile>");
		STSUtil util = new STSUtil();
		AWSSecurityTokenService sts = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, profile);
		GetCallerIdentityResult result = util.getCallerId(sts);
		System.out.println("Account:\t"+result.getAccount());
		System.out.println("User:\t\t"+result.getUserId());
		System.out.println("ARN:\t\t"+result.getArn());
	}
	
	public void demoStsFederation(String username) throws Exception{
		h.help(username,"<username>");
		STSUtil util = new STSUtil();
		AWSSecurityTokenService stsChina = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, "china");
		Policy policy = new Policy()
		.withStatements(
				new Statement(Effect.Allow)
				.withActions(S3Actions.GetObject)
				.withResources(new Resource("*")));
		System.out.println("Calling user: "+stsChina.getCallerIdentity(new GetCallerIdentityRequest()).getArn());
		System.out.println("Setting policy: ");
		System.out.println(policy.toJson());
		BasicSessionCredentials bsc3 = null;
		try{
			System.out.println("Calling GetFederationToken...");
			bsc3 = util.getFederatedUserToken(stsChina, 900, username, policy);
			System.out.println("Temp Access Key Id: "+bsc3.getAWSAccessKeyId());
			System.out.println("Temp Secret Key: "+bsc3.getAWSSecretKey());
			System.out.print("Temp Session Token: "+bsc3.getSessionToken());
		}catch(AmazonServiceException ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void demoSts(String tokenCode) throws Exception{
		h.help(tokenCode,"<token-code-of-mfa>");
		int durationSec = 900;
		if(tokenCode.equals("------")){
			durationSec=999999999;
		}
		STSUtil util = new STSUtil();
		
		h.title("GetSessionToken /w MFA from Permanent Crendentials");
		AWSSecurityTokenService stsPawnGlobal = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, Config.MFA_USERNAME);
		try{
			System.out.println("Calling user: "+stsPawnGlobal.getCallerIdentity(new GetCallerIdentityRequest()).getArn());
			BasicSessionCredentials bsc1 = util.getSessionTokenMFA(stsPawnGlobal, durationSec, Config.MFA_ARN, tokenCode);
			System.out.println("Temp AK: "+bsc1.getAWSAccessKeyId());
		}catch (AmazonServiceException ex){
			System.out.println(ex.getMessage());
		}
		
		AWSSecurityTokenService stsChina = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, "beijing");
		h.title("GetSessionToken from Permanent Credentials");
		System.out.println("Calling user: "+stsChina.getCallerIdentity(new GetCallerIdentityRequest()).getArn());
		try{
			BasicSessionCredentials bsc2 = util.getSessionToken(stsChina, durationSec);
			System.out.println("Temp AK: "+bsc2.getAWSAccessKeyId());
		}catch(AmazonServiceException ex){
			System.out.println(ex.getMessage());
		}
		
		h.title("GetFederatedUserToken from Permanent Credentials");
		Policy policy = new Policy()
						.withStatements(
								new Statement(Effect.Allow)
								.withActions(S3Actions.GetObject)
								.withResources(new Resource("*")));
		System.out.println("Calling user: "+stsChina.getCallerIdentity(new GetCallerIdentityRequest()).getArn());
		BasicSessionCredentials bsc3 = null;
		try{
			bsc3 = util.getFederatedUserToken(stsChina, durationSec, "batman007", policy);
			System.out.println("Temp AK: "+bsc3.getAWSAccessKeyId());
		}catch(AmazonServiceException ex){
			System.out.println(ex.getMessage());
		}
		
		h.title("AssumeRole from Permanent Credentials");
		//String assumedUserArn = null;
		System.out.println("Calling user: "+stsChina.getCallerIdentity(new GetCallerIdentityRequest()).getArn());
		BasicSessionCredentials bsc4 = null;
		try{
			bsc4 = util.assumeRole(stsChina, durationSec, Config.EXAMPLE_IAM_ROLE_ARN, "developer");
			System.out.println("Temp AK: "+bsc4.getAWSAccessKeyId());
		}catch(AmazonServiceException ex){
			System.out.println(ex.getMessage());
		}
		
		h.title("AssumeRole after AssumeRole");
		AWSSecurityTokenService stsRole = new AWSSecurityTokenServiceClient(bsc4);
		stsRole.setRegion(Region.getRegion(Regions.CN_NORTH_1));
		try{
			System.out.println("Calling user: "+stsRole.getCallerIdentity(new GetCallerIdentityRequest()).getArn());
			BasicSessionCredentials bsc14 = util.assumeRole(stsChina, durationSec, Config.EXAMPLE_IAM_ROLE_ARN, "developer");
			System.out.println("AK: "+bsc14.getAWSAccessKeyId());
		}catch(AmazonServiceException ex){
			System.out.println(ex.getMessage());
		}
		
		h.title("GetFederatedUserToken after AssumeRole");
		AWSSecurityTokenService stsTemp = new AWSSecurityTokenServiceClient(bsc4);
		stsTemp.setRegion(Region.getRegion(Regions.CN_NORTH_1));
		try{
			System.out.println("Calling user: "+stsTemp.getCallerIdentity(new GetCallerIdentityRequest()).getArn());
			BasicSessionCredentials bsc5 = util.getFederatedUserToken(stsTemp, durationSec, "batman007", new Policy()
																								.withStatements(
																									new Statement(Effect.Allow)
																									.withActions(S3Actions.GetObject)
																									.withResources(new Resource("*"))));
			System.out.println("AK: "+bsc5.getAWSAccessKeyId());
		}catch(AmazonServiceException ex){
			System.out.println(ex.getMessage());
		}
		
		h.title("AssumeRole after GetFederatedUserToken");
		AWSSecurityTokenService stsFed = new AWSSecurityTokenServiceClient(bsc3);
		stsFed.setRegion(Region.getRegion(Regions.CN_NORTH_1));
		try{
			System.out.println("Calling user: "+stsFed.getCallerIdentity(new GetCallerIdentityRequest()).getArn());
			BasicSessionCredentials bsc10 = util.assumeRole(stsFed, durationSec, Config.EXAMPLE_IAM_ROLE_ARN, "batman007");
			System.out.println("Temp AK: "+bsc10.getAWSAccessKeyId());
		}catch (AmazonServiceException ex){
			System.out.println(ex.getMessage());
		}
		
		
		AWSSecurityTokenService stsIp = new AWSSecurityTokenServiceClient(new InstanceProfileCredentialsProvider());
		stsIp.setRegion(Region.getRegion(Regions.CN_NORTH_1));
		h.title("GetSessionToken from Instance Profile");
		System.out.println("Calling user: EC2 Instance Profile");
		try{
			BasicSessionCredentials bsc6 = util.getSessionToken(stsIp, 900);
			System.out.println("Temp AK: "+bsc6.getAWSAccessKeyId());
		}catch(AmazonClientException ex){
			System.out.println(ex.getMessage());
		}
		
		h.title("GetFederatedUserToken from Instance Profile");
		System.out.println("Calling user: EC2 Instance Profile");
		Policy policyx = new Policy()
		.withStatements(
				new Statement(Effect.Allow)
				.withActions(S3Actions.GetObject)
				.withResources(new Resource("*")));
		try{
			BasicSessionCredentials bsc7 = util.getFederatedUserToken(stsIp, 900, "batman", policyx);
			System.out.println("Temp AK: "+bsc7.getAWSAccessKeyId());
		}catch(AmazonClientException ex){
			System.out.println(ex.getMessage());
		}
		
		h.title("AssumeRole from Instance Profile");
		System.out.println("Calling user: EC2 Instance Profile");
		try{
			BasicSessionCredentials bsc8 = util.assumeRole(stsIp, 900, Config.EXAMPLE_IAM_ROLE_ARN, "one-ec2-instance");
			System.out.println("Temp AK: "+bsc8.getAWSAccessKeyId());
		}catch(AmazonClientException ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void showResourceAll() throws Exception{
		GeneralUtil.showAllResource();
	}
	
	public void showResourceByProfile(String profile) throws Exception{
		h.help(profile,"<profile>");
		GeneralUtil.showAllResourceInProfileV2(profile);
	}
	
	public void showResourceByService(String serviceAbbr) throws Exception{
		/*
		1: Autoscaling - autoscaling // Done
		2: CloudFormation - cloudformation //Done
		3: CloudFront - cloudfront // Done
		4: CloudHSM - cloudhsm // Done
		5: CloudSearch - cloudsearch // Done
		6: CloudTrail - cloudtrail // Done
		7: CloudWatch - monitoring // Done
		8: CloudWatchLogs - logs	// Done
		9: CodeCommit - codecommit
		10: CodeDeploy - codedeploy // Done
		11: CodePipeline - codepipeline
		12: CognitoIdentity - cognito-identity
		13: CognitoIdentityProvider - cognito-idp
		14: CognitoSync - cognito-sync
		15: Config - config // Done
		16: DMS - dms
		17: DataPipeline - datapipeline
		18: DeviceFarm - devicefarm
		19: DirectConnect - directconnect
		20: Directory - ds
		21: Dynamodb - dynamodb
		22: DynamodbStreams - streams.dynamodb
		23: EC2 - ec2 // Done
		24: EC2ContainerService - ecs
		25: EC2SimpleSystemsManager - ssm
		26: ElasticBeanstalk - elasticbeanstalk
		27: ElasticFileSystem - elasticfilesystem
		28: ElasticLoadbalancing - elasticloadbalancing // Done
		29: ElasticMapReduce - elasticmapreduce // Done
		30: ElasticTranscoder - elastictranscoder
		31: Elasticache - elasticache // Done
		32: Email - email
		33: GameLift - gamelift
		34: Glacier - glacier // Done
		35: IAM - iam // Done
		36: ImportExport - importexport
		37: IoT - iot
		38: IoTData - data.iot
		39: KeyManagementService - kms // Done
		40: Kinesis - kinesis // Done
		41: Lambda - lambda // Done
		42: MachineLearning - machinelearning
		43: Opsworks - opsworks
		44: RDS - rds // Done
		45: RedShift - redshift
		46: Route53 - route53
		47: Route53Domains - route53domains
		48: S3 - s3 // Done
		49: SNS - sns // Done
		50: SQS - sqs // Done
		51: STS - sts // N/A
		52: SimpleDB - sdb
		53: SimpleWorkflow - swf
		54: StorageGateway - storagegateway
		55: Support - support
		56: WAF - waf
		57: Workspaces - workspaces
		*/
		h.help(serviceAbbr,"<service-abbr>");
		Object c = null;
		if(serviceAbbr.equals(AmazonIdentityManagement.ENDPOINT_PREFIX)){
			h.title("[china]");
			c = Clients.getClientByServiceAbbrProfile(serviceAbbr, "beijing");
			new IAMUtil().printAllPhysicalId(c);
			h.title("[global]");
			c = Clients.getClientByServiceAbbrProfile(serviceAbbr, "mumbai");
			new IAMUtil().printAllPhysicalId(c);
			System.out.println("\nBye~");
			return;
		}
		if(serviceAbbr.equals(AmazonS3.ENDPOINT_PREFIX)){
			h.title("[china]");
			c = Clients.getClientByServiceAbbrProfile(serviceAbbr, "beijing");
			new S3Util().printAllPhysicalId(c);
			h.title("[global]");
			c = Clients.getClientByServiceAbbrProfile(serviceAbbr, "virginia");
			new S3Util().printAllPhysicalId(c);
			System.out.println("\nBye~");
			return;
		}
		for(String profile:GeneralUtil.SHOW_SERVICE_PROFILES){
			if(GeneralUtil.isGlobalService(serviceAbbr) && !GeneralUtil.isChinaProfile(profile)){
				c = Clients.getClientByServiceAbbrProfile(serviceAbbr, "mumbai");
				switch (serviceAbbr){
				case AmazonCloudFront.ENDPOINT_PREFIX: new CloudFrontUtil().printAllPhysicalId(c); break;
				}
				break;
			}
			if(!GeneralUtil.isCompatible(serviceAbbr, profile)){continue;}
			try{
				c = Clients.getClientByServiceAbbrProfile(serviceAbbr, profile);
				h.title("["+profile+"]");
				switch (serviceAbbr){
				case AmazonAutoScaling.ENDPOINT_PREFIX: new AutoscalingUtil().printAllPhysicalId(c); break;
				case AmazonCloudFormation.ENDPOINT_PREFIX: new CloudFormationUtil().printAllPhysicalId(c); break;
				case AWSCloudHSM.ENDPOINT_PREFIX: new CloudHSMUtil().printAllPhysicalId(c); break;
				case AmazonCloudSearch.ENDPOINT_PREFIX: new CloudSearchUtil().printAllPhysicalId(c); break;
				case AWSCloudTrail.ENDPOINT_PREFIX: new CloudTrailUtil().printAllPhysicalId(c); break;
				case AWSCodeCommit.ENDPOINT_PREFIX: new CodeCommitUtil().printAllPhysicalId(c); break;
				case AmazonCloudWatch.ENDPOINT_PREFIX: new CloudWatchUtil().printAllPhysicalId(c); break;
				case AWSLogs.ENDPOINT_PREFIX: new CloudWatchLogsUtil().printAllPhysicalId(c); break;
				case AmazonCodeDeploy.ENDPOINT_PREFIX: new CodeDeployUtil().printAllPhysicalId(c); break;
				case AmazonConfig.ENDPOINT_PREFIX: new ConfigUtil().printAllPhysicalId(c); break;
				case AmazonEC2.ENDPOINT_PREFIX: new EC2Util().printAllPhysicalId(c); break;
				case AmazonElasticLoadBalancing.ENDPOINT_PREFIX: new ElasticLoadbalancingUtil().printAllPhysicalId(c); break;
				case AmazonElasticMapReduce.ENDPOINT_PREFIX: new ElasticMapReduceUtil().printAllPhysicalId(c); break;
				case AmazonElastiCache.ENDPOINT_PREFIX: new ElasticacheUtil().printAllPhysicalId(c); break;
				case AmazonGlacier.ENDPOINT_PREFIX: new GlacierUtil().printAllPhysicalId(c); break;
				case AWSKMS.ENDPOINT_PREFIX: new KeyManagementServiceUtil().printAllPhysicalId(c); break;
				case AmazonKinesis.ENDPOINT_PREFIX: new KinesisUtil().printAllPhysicalId(c); break;
				case AWSLambda.ENDPOINT_PREFIX: new LambdaUtil().printAllPhysicalId(c); break;
				case AmazonRDS.ENDPOINT_PREFIX: new RDSUtil().printAllPhysicalId(c); break;
				case AmazonSNS.ENDPOINT_PREFIX: new SNSUtil().printAllPhysicalId(c); break;
				case AmazonSQS.ENDPOINT_PREFIX: new SQSUtil().printAllPhysicalId(c); break;
				default: System.out.println("Service name: "+serviceAbbr+" unkown.");
				}
			}catch(AmazonClientException ex){
				System.out.println("error: "+ex.getMessage());
			}
		}
		System.out.println("\nBye~");
	}
	
	public void showSqsMessage(String queueName, String profile) throws Exception{
		h.help(queueName,"<queue-name> <profile>");
		SQSUtil util = new SQSUtil();
		AmazonSQS sqs = (AmazonSQS) Clients.getClientByServiceClassProfile(Clients.SQS, profile);
		AWSSecurityTokenService sts = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, profile);
		util.showAllMessageInQueue(sqs, queueName, 7, profile);
	}
	
	public void kinesisShowShard(String streamName, String profile) throws Exception{
		h.help(streamName,"<stream-name> <profile>");
		KinesisUtil util = new KinesisUtil();
		AmazonKinesis k = (AmazonKinesis) Clients.getClientByServiceClassProfile(Clients.KINESIS, profile);
		util.printShardInStream(k, streamName);
	}
	
	public void kinesisSplitShardInHalf(String streamName, String shardId, String profile) throws Exception{
		h.help(streamName,"<stream-name> <shard-id> <profile>");
		KinesisUtil util = new KinesisUtil();
		AmazonKinesis k = (AmazonKinesis) Clients.getClientByServiceClassProfile(Clients.KINESIS, profile);
		util.splitShardInHalf(k, streamName, shardId);
	}
	
	public void kinesisStreamFission(String streamName, String profile) throws Exception{
		h.help(streamName,"<stream-name> <profile>");
		KinesisUtil util = new KinesisUtil();
		AmazonKinesis k = (AmazonKinesis) Clients.getClientByServiceClassProfile(Clients.KINESIS, profile);
		util.streamFission(k, streamName);
	}
	
	public void kinesisStreamCollapse(String streamName, String profile) throws Exception{
		h.help(streamName,"<stream-name> <profile>");
		KinesisUtil util = new KinesisUtil();
		AmazonKinesis k = (AmazonKinesis) Clients.getClientByServiceClassProfile(Clients.KINESIS, profile);
		util.streamCollapse(k, streamName);
	}
	
	public void kinesisMergeShard(String streamName, String shardId1, String shardId2, String profile) throws Exception{
		h.help(streamName,"<stream-name> <shard-id1> <shard-id2> <profile>");
		KinesisUtil util = new KinesisUtil();
		AmazonKinesis k = (AmazonKinesis) Clients.getClientByServiceClassProfile(Clients.KINESIS, profile);
		util.mergeShards(k, streamName, shardId1, shardId2);
	}
	
	public void kinesisProduceRandomRecord(String streamName, String dop, String recordsPerPut, String profile){
		h.help(streamName,"<stream-name> <dop> <records-per-put> <profile>");
		KinesisUtil util = new KinesisUtil();
		util.produceRandomRecords(streamName, Integer.parseInt(dop), Integer.parseInt(recordsPerPut), profile);
	}
	
	public void kinesisProduceRandomUserRecordByKpl(String streamName, String connections, String userRecordsToAggregate, String recordsPerPut, String profile, String tempDir){
		h.help(streamName,"<stream-name> <connections> <user-records-to-aggregate> <records-per-put> <profile> <temp-dir>");
		KinesisUtil util = new KinesisUtil();
		util.produceRandomRecordsByKPL(streamName, Integer.parseInt(connections), Integer.parseInt(userRecordsToAggregate), Integer.parseInt(recordsPerPut), profile, tempDir);
	}
	
	public void kinesisConsumeRandomRecordByKcl(String streamName, String initialPositionInStream, String initStyle, String profile) throws Exception{
		h.help(streamName,"<stream-name> <initial-position-in-stream: latest|trim_horizon> <initial-style: join|new> <profile>");
		KinesisUtil util = new KinesisUtil();
		InitialPositionInStream ipis = null;
		if(initialPositionInStream.equals("latest")){
			ipis = InitialPositionInStream.LATEST;
		}
		else if (initialPositionInStream.equals("trim_horizon")){
			ipis = InitialPositionInStream.TRIM_HORIZON;
		}
		else{
			System.out.println("Invalid InitialPositionInStream, available values: latest | trim_horizon.");
			return;
		}
		util.consumeRandomRecordsByKCL(streamName, ipis, initStyle, profile);
	}
	
	public void showServiceEndpoint(String regionName) throws Exception{
		h.help(regionName,"<region-name>");
		Region region = Region.getRegion(Regions.fromName(regionName));
		TreeSet<String> ts = new TreeSet<String>(GeneralUtil.SERVICENAME_PACKAGECLASS.keySet());
		String endpoint = null;
		int i=1;
		for(String serviceName:ts){
			endpoint = region.getServiceEndpoint(serviceName);
			if(endpoint!=null && !endpoint.equals("null")){
				System.out.println(i+++") "+serviceName+":             \t"+endpoint);
			}
		}
	}
	
	/**
	 * Regionally drop all resources with a dropme prefix.
	 * A prefix is the value used in tag "Name", or the resource name if tagging is NOT available.
	 */
	public void troll(String objectPrefixToClean, String profile) throws Exception{
		h.help(objectPrefixToClean,"<object-prefix-to-clean: dropme|launch-wizard|qlstack> <profile>");
		if(!objectPrefixToClean.startsWith("dropme") && !objectPrefixToClean.startsWith("launch-wizard") && !objectPrefixToClean.startsWith("qlstack")){
				System.out.println("<object-prefix> must start with dropme -or- launch-wizard -or- qlstack");
				return;
		}
		this.dropmeCfn(objectPrefixToClean, profile);
		this.dropmeAsg(objectPrefixToClean, profile); 
		this.dropmeLc(objectPrefixToClean, profile);
		this.dropmeEc2(objectPrefixToClean, profile);
		this.dropmeKeyPair(objectPrefixToClean, profile);
		this.dropmeElb(objectPrefixToClean, profile);
		this.dropmeElastiCache(objectPrefixToClean, profile);
		this.dropmeSg(objectPrefixToClean, profile);
		this.dropmeSns(objectPrefixToClean, profile);
		this.dropmeSqs(objectPrefixToClean, profile);
		this.dropmeSnapshot(objectPrefixToClean, profile);
		this.dropmeEbs(objectPrefixToClean, profile);
		this.dropmeVpc(objectPrefixToClean, profile);
		this.dropmeIamResource(objectPrefixToClean,profile);
		this.dropmeAmi(objectPrefixToClean,profile);
		this.dropmeEmr(objectPrefixToClean,profile);
		this.dropmeRt(objectPrefixToClean,profile);
		this.dropmeEni(objectPrefixToClean, profile);
		this.dropmeKinesis(objectPrefixToClean, profile);
		this.dropmeDdb(objectPrefixToClean, profile);
		this.dropmeRds(objectPrefixToClean, profile);
		this.dropmeRdsSnapshot(objectPrefixToClean, profile);
		System.out.println("Troll RTB");
	}
	
	public void dropmeRds(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop RDS DB instance with DB id prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonRDS rds = (AmazonRDS) Clients.getClientByServiceClassProfile(Clients.RDS, profile);
		RDSUtil util = new RDSUtil();
		for(DBInstance i:rds.describeDBInstances().getDBInstances()){
			if(i.getDBInstanceIdentifier().startsWith(prefix)){
				util.deleteDBInstance(rds, i.getDBInstanceIdentifier());
			}
		}
	}
	
	public void dropmeKinesis(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop Kinesis with stream name prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonKinesis k = (AmazonKinesis) Clients.getClientByServiceClassProfile(Clients.KINESIS, profile);
		KinesisUtil util = new KinesisUtil();
		for(String streamName:k.listStreams().getStreamNames()){
			if(streamName.startsWith(prefix)){
				util.deleteStream(k, streamName);
			}
		}
	}
	
	public void dropmeRdsSnapshot(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop RDS snapshot with id prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonRDS rds = (AmazonRDS) Clients.getClientByServiceClassProfile(Clients.RDS, profile);
		RDSUtil util = new RDSUtil();
		for(DBSnapshot ss:rds.describeDBSnapshots().getDBSnapshots()){
			if(ss.getDBSnapshotIdentifier().startsWith(prefix)){
				util.deleteDBSnapshot(rds, ss.getDBSnapshotIdentifier());
			}
		}
	}
	
	public void dropmeDdb(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop DDB table with name prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		for(String tableName:ddb.listTables(new ListTablesRequest()).getTableNames()){
			if(tableName.startsWith(prefix)){
				new DynamodbUtil().deleteTable(ddb, tableName);
			}
		}
	}
	
	//TODO com.amazonaws.services.cloudfront.model.InvalidIfMatchVersionException: The If-Match version is missing or not valid for the resource.
	public void dropmeCf(String id) throws Exception{
		h.help(id,"<ditribution-id>");
		System.out.println("> Drop Cloudfront Distribution with ID: "+id);
		AmazonCloudFront cf = (AmazonCloudFrontClient) Clients.getClientByServiceClassProfile(Clients.CF, "mumbai");
		new CloudFrontUtil().deleteDistribution(cf, id);
	}
	
	public void dropmeEni(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop ENI with tag name prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		Filter f = new Filter().withName("tag:Name").withValues(prefix+"*");
		EC2Util util = new EC2Util();
		util.dropeEniByFilter(ec2, f);
	}
	
	public void dropmeRt(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop Route Table with name prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		Filter f = new Filter().withName("tag:Name").withValues(prefix+"*");
		for(RouteTable rt:ec2.describeRouteTables(new DescribeRouteTablesRequest().withFilters(f)).getRouteTables()){
			System.out.println("=> Deleting route table: "+rt.getRouteTableId());
			ec2.deleteRouteTable(new DeleteRouteTableRequest().withRouteTableId(rt.getRouteTableId()));
		}
	}
	
	public void dropmeEmr(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop EMR with name prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonElasticMapReduce emr = (AmazonElasticMapReduce) Clients.getClientByServiceClassProfile(Clients.EMR, profile);
		ElasticMapReduceUtil util = new ElasticMapReduceUtil();
		List<ClusterSummary> list = util.getAliveEmrClusters(emr);
		for(ClusterSummary cs:list){
			if(cs.getName().startsWith(prefix)){
				util.terminateEmrCluster(emr, cs.getId());
			}
		}
	}
	
	public void dropmeAmi(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop AMI with name prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		for(Image i:ec2.describeImages(new DescribeImagesRequest().withOwners("self")).getImages()){
			if(i.getName().startsWith(prefix)){
				util.deregisterAmi(ec2, i.getImageId());
			}
		}
	}
	
	public void dropmeCfn(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop CFN stack with name prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		CloudFormationUtil util = new CloudFormationUtil();
		AmazonCloudFormation cfn = (AmazonCloudFormation) Clients.getClientByServiceClassProfile(Clients.CFN, profile);
		ArrayList<String> targetStack = new ArrayList<String>();
		for(Stack s: cfn.describeStacks().getStacks()){
			if(s.getStackName().startsWith(prefix)){
				targetStack.add(s.getStackName());
				util.deleteCNFStack(cfn, s.getStackName());
			}
		}
		boolean test = true;
		int tests = 24;
		List<Stack> stacks = null;
		while(targetStack.size()>0 && test){
			tests--;
			if(tests==0){System.out.println("Timed out, please check the status manually"); break;}
			Thread.sleep(5000);
			for(String stackName:targetStack){
				stacks = cfn.describeStacks(new DescribeStacksRequest().withStackName(stackName)).getStacks();
				if(stacks!=null && stacks.size()>0){
					test = true;
					System.out.println("Waiting for deleting "+stackName);
					break;
				}
				test = false;
			}
		}
		if(!test){
			for(String stackName: targetStack){
				System.out.println("Stack "+stackName+" deleted");
			}
		}
	}
	
	public void dropmeKeyPair(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop Keypair with prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		EC2Util util = new EC2Util();
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		List<KeyPairInfo> keys = ec2.describeKeyPairs().getKeyPairs();
		for(KeyPairInfo key:keys){
			if (key.getKeyName().startsWith(prefix)){
				util.dropKeypair(ec2, key.getKeyName());
			}
		}
	}
	
	public void dropmeLc(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop LC with prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AutoscalingUtil util = new AutoscalingUtil();
		AmazonAutoScaling asg = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
		List<LaunchConfiguration> lcs = asg.describeLaunchConfigurations().getLaunchConfigurations();
		for(LaunchConfiguration lc:lcs){
			if(lc.getLaunchConfigurationName().startsWith(prefix)){
				util.deleteLcByName(asg, lc.getLaunchConfigurationName());
			}
		}
	}
	
	public void dropmeIamResource(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop IAM resources with prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		IAMUtil util = new IAMUtil();
		AmazonIdentityManagement iam = (AmazonIdentityManagement) Clients.getClientByServiceClassProfile(Clients.IAM, profile);
		for(Role role:iam.listRoles().getRoles()){
			if(role.getRoleName().startsWith(prefix)){
				System.out.println("=> Dropping Role: "+role.getRoleName());
				util.dropRole(iam, role.getRoleName());
			}
		}
		for(InstanceProfile ip:iam.listInstanceProfiles().getInstanceProfiles()){
			if(ip.getInstanceProfileName().startsWith(prefix)){
				System.out.println("=> Dropping Instance Profile: "+ip.getInstanceProfileName());
				util.dropInstanceProfile(iam, ip.getInstanceProfileName());
			}
		}
		for(User user:iam.listUsers().getUsers()){
			if(user.getUserName().startsWith(prefix)){
				System.out.println("=> Dropping User: "+user.getUserName());
				util.dropUser(iam, user.getUserName());
			}
		}
		for(Group group:iam.listGroups().getGroups()){
			if(group.getGroupName().startsWith(prefix)){
				System.out.println("=> Dropping Group: "+group.getGroupName());
				util.dropGroup(iam, group.getGroupName());
			}
		}
	}
	
	/**
	 * Drop ASG by ASG name prefix.
	 * @param prefix
	 * @param profile
	 * @throws Exception
	 */
	public void dropmeAsg(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop Auto Scaling Group with prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AutoscalingUtil util = new AutoscalingUtil();
				AmazonAutoScaling asg = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
				try{
					for(AutoScalingGroup group:asg.describeAutoScalingGroups().getAutoScalingGroups()){
						if(group.getAutoScalingGroupName().startsWith(prefix)){
							util.deleteAsgByName(asg, group.getAutoScalingGroupName());
						}
					}
				}
				catch(AmazonServiceException ex){
					System.out.println(ex.getMessage());
				}
	}
	
	/**
	 * Drop ElastiCache by ElastiCache cluster id prefix.
	 * @param prefix
	 * @param profile
	 * @throws Exception
	 */
	public void dropmeElastiCache(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop ElastiCache Cluster with prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		ElasticacheUtil util = new ElasticacheUtil();
		AmazonElastiCache cache = (AmazonElastiCache) Clients.getClientByServiceClassProfile(Clients.ELASTICACHE, profile);
		List<CacheCluster> clusters = cache.describeCacheClusters().getCacheClusters();
		for(CacheCluster cc:clusters){
			if (cc.getCacheClusterId().startsWith(prefix)){
				util.dropCacheClusterById(cache, cc.getCacheClusterId());
			}
		}
	}
	
	public void dropmeSqs(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop SQS with prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonSQS sqs = (AmazonSQS) Clients.getClientByServiceClassProfile(Clients.SQS, profile);
		for(String url:sqs.listQueues().getQueueUrls()){
			if(url.matches(".*/"+prefix+"[^/]*")){
				System.out.println("=> Dropping queue "+url);
				sqs.deleteQueue(new DeleteQueueRequest()
										.withQueueUrl(url));
			}
		}
	}
	
	public void dropmeSns(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop SNS with prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonSNS sns = (AmazonSNS) Clients.getClientByServiceClassProfile(Clients.SNS, profile);
		//SNSUtil util = new SNSUtil();
		List<Topic> topics = sns.listTopics().getTopics();
		for(Topic t:topics){
			if(t.getTopicArn().matches(".*:"+prefix+"[^:]*")){
				System.out.println("=> Dropping topic "+t.getTopicArn());
				sns.deleteTopic(new DeleteTopicRequest().withTopicArn(t.getTopicArn()));
			}
		}
	}
	
	public void dropmeVpc(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop VPC with tag prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		Filter f = new Filter().withName("tag:Name").withValues(prefix+"*");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		util.dropVpcByFilter(ec2, f);
	}
	
	public void dropmeEbs(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop EBS with tag prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		Filter f = new Filter().withName("tag:Name").withValues(prefix+"*");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		util.dropEbsByFilter(ec2, f);
	}
	
	public void dropmeSnapshot(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop Snapshot with tag prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		Filter f = new Filter().withName("tag:Name").withValues(prefix+"*");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		util.dropSnapshotByFilter(ec2,f);
	}
	
	public void dropmeElb(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop ELB with prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		AmazonElasticLoadBalancing elb = (AmazonElasticLoadBalancing) Clients.getClientByServiceClassProfile(Clients.ELB, profile);
		ElasticLoadbalancingUtil util = new ElasticLoadbalancingUtil();
		List<LoadBalancerDescription> descs = elb.describeLoadBalancers().getLoadBalancerDescriptions();
		for(LoadBalancerDescription desc:descs){
			if(desc.getLoadBalancerName().startsWith(prefix)){
				util.deleteElbByName(elb, desc.getLoadBalancerName());
			}
		}
	}
	
	public void dropmeSg(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop SG with name prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		Filter f = new Filter().withName("group-name").withValues(prefix+"*");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		util.dropSecurityGroupByFilter(ec2, f);
	}
	
	public void dropmeEc2(String prefix, String profile) throws Exception{
		h.help(prefix,"<object-prefix-to-clean> <profile>");
		System.out.println("> Drop EC2 with tag prefix "+prefix+"* in "+Clients.getRegionCode(profile));
		Filter f = new Filter().withName("tag:Name").withValues(prefix+"*");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		util.terminateInstancesByFilter(ec2, f);		
	}
	/**
	 * XxxUtil to be implemented for Biu!
	 */
	public void showGap(){
		h.title("Util //TODO");
		for(String abbr:Biu.SERVICEABB_SERVICENAME.keySet()){
			String serviceName = Biu.SERVICEABB_SERVICENAME.get(abbr);
			if(!GeneralUtil.SERVICENAME_PACKAGECLASS.containsKey(serviceName)){
				System.out.println(abbr+" - "+Biu.SERVICEABB_SERVICENAME.get(abbr));
			}
		}
	}
	
	public void showActionByService(String serviceAbbr) throws Exception{
		h.help(serviceAbbr,"<service-name-abbr>");
		Object u = Clients.getClientByServiceAbbrProfile(serviceAbbr,"global");
		Class<?> clazz = u.getClass();
		System.out.println("# Actions can be called by "+clazz.getCanonicalName());
		Method[] methods = clazz.getDeclaredMethods();
		TreeSet<String> ts = new TreeSet<String>(); 
		for(Method m:methods){
			if(Modifier.isPublic(m.getModifiers())){
				ts.add(m.getName());
			}
		}
		Iterator<String> it = ts.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	
	public void purgeBucket(String bucketName, String profile) throws Exception{
		h.help(bucketName,"<bucket> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		try{
			util.purgeBucket(s3, bucketName);
		}catch (AmazonS3Exception ex){
			if(ex.getMessage().indexOf("XML you provided was not well-formed") > 0){
				System.out.println("I don't care about: "+ex.getMessage());
				Thread.sleep(1000);
				this.purgeBucket(bucketName,profile);
			}
		}
	}
	
	public void deleteBucketForce(String bucketName, String profile) throws Exception{
		h.help(bucketName,"<bucket> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		this.purgeBucket(bucketName,profile);
		util.deleteBucketForce(s3, bucketName);
	}
	
	public void clearMultipartUploadTrash(String bucketName, String profile) throws Exception{
		h.help(bucketName,"<bucket> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		TransferManager tm = new TransferManager(s3); 
		S3Util util = new S3Util();
		util.clearMultipartTrash(tm, bucketName);
	}
	
	public void uploadFileMultipartSizeParallel(String regionPartition, String bucketName, String key, String filePath, String partSizeInMB, String dop) throws Exception{
		h.help(regionPartition, "<region-partition: china|others> <bucket> <key> <local-file-path> <part-size-in-mb> <degree-of-parallel>");
		S3Util util = new S3Util();
		util.uploadFileMultipartSizeParallel(regionPartition, new File(filePath), bucketName, key, Integer.parseInt(partSizeInMB), Integer.parseInt(dop));
	}
	
	public void uploadFileMultipartCountParallel(String regionPartition, String bucketName, String key, String filePath, String partCount, String dop) throws Exception{
		h.help(regionPartition,"<region-partition: china|others> <bucket> <key> <local-file-path> <part-count> <degree-of-parallel>");
		S3Util util = new S3Util();
		util.uploadFileMultipartParallel(regionPartition, new File(filePath), bucketName, key, Integer.parseInt(partCount), Integer.parseInt(dop));
	}
	
	public void uploadFileMultipart(String bucketName, String key, String filePath, String partCount, String profile) throws Exception{
		h.help(bucketName,"<bucket> <key> <local-file-path> <part-count> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		util.uploadFileMultipart(s3, new File(filePath), bucketName, key, Integer.parseInt(partCount));
	}
	
	public void uploadFile(String bucketName, String objectKey, String filePath, String profile) throws Exception{
		h.help(bucketName,"<bucket> <key> <local-file-path> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		util.uploadFile(s3, new File(filePath), bucketName, objectKey);
	}
	
	public void uploadFileWithCustomerKey(String bucketName, String objectKey, String filePath, String customerKeySerDePath, String profile) throws Exception{
		h.help(bucketName,"<bucket> <key> <local-file-path> <customerKeySerDePath> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		util.uploadFileWithCustomerEncryptionKey(s3, new File(filePath), bucketName, objectKey, customerKeySerDePath);
	}
	
	public void downloadFile(String bucketName, String key, String filePath, String profile) throws Exception{
		h.help(bucketName,"<bucket> <key> <local-file-path> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		util.downloadFile(s3, bucketName, key, new File(filePath));
	}
	
	public void downloadFileWithCustomerKey(String bucketName, String key, String filePath, String customerKeySerDePath, String profile) throws Exception{
		h.help(bucketName,"<bucket> <key> <local-file-path> <customerKeySerDePath>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		util.downloadFileWithCustomerEncryptionKey(s3, bucketName, key, new File(filePath), customerKeySerDePath);
	}
	
	public void downloadFileAnonymous(String regionPartition, String bucketName, String key, String filePath) throws Exception{
		h.help(regionPartition,"<region-partition: china|others> <bucket> <key> <local-file-path>");
		S3Util util = new S3Util();
		if(regionPartition.equals("china")){
			util.downloadFileAnonymousChina(bucketName, key, new File(filePath));
		}
		else{
			util.downloadFileAnonymousGlobal(bucketName, key, new File(filePath));
		}
	}
	
	public void showAmiByPrefix(String amiNamePrefix, String profile) throws Exception{
		h.help(amiNamePrefix,"<ami-name-prefix> <profile>");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		List<Image> images = util.getAmiByName(ec2, amiNamePrefix);
		for(Image image:images){
			System.out.println(image.getName()+", "+image.getImageId()+", "+image.getCreationDate()+", "+image.getImageLocation());
		}
	}
	
	public void removeAmiById(String amiId, String profile) throws Exception{
		h.help(amiId,"<ami-id> <profile>");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		util.deregisterAmi(ec2, amiId);
		System.out.println("Removing AMI: "+amiId+" requested");
	}
	
	public void ddbDeleteGsi(String tableName, String indexName, String profile) throws Exception{
		h.help(tableName,"<table-name> <index-name> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		util.deleteGsi(ddb, indexName, tableName);
	}
	
	public void ddbCreateGsi(String tableName, String indexName, String hashName, String rangeName, String rcu, String wru, String profile) throws Exception{
		h.help(tableName,"<table-name> <index-name> <hash-attr-name> <range-attr-name> <read-unit> <write-unit> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		String rangeKeyName = rangeName.equals("null")?null:rangeName;
		util.createGsiString(ddb, indexName, tableName, hashName, rangeKeyName, Long.parseLong(rcu), Long.parseLong(wru));
	}
	
	public void ddbUpdateItemByPkHashString(String regionName, String tableName, String hashName, String hashValue, String updateExpression, String profile) throws Exception{
		h.help(regionName,"<table-name> <hash-pk-name> <hash-pk-value> <update-expression|replace space with ^> <profile>");
		String expandedUpdateExpression = updateExpression.replaceAll("\\^", " ");
		System.out.println("UpdateExpression: "+expandedUpdateExpression);
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		util.updateItemByPkHashString(ddb, tableName, hashName, hashValue, expandedUpdateExpression);
	}
	
	public void ddbScanItemByFilterAsync(String tableName, String filterName, String filterValue, String profile) throws Exception{
		h.help(tableName,"<table-name> <filter-name> <filter-value> <profile>");
		AmazonDynamoDBAsync ddb = (AmazonDynamoDBAsync) Clients.getClientByServiceClassProfile(Clients.DDBAsync, "profile");
		DynamodbUtil util = new DynamodbUtil();
		util.scanItemByFilterAsync(ddb,tableName,filterName,filterValue);
	}
	
	public void ddbScanItemByFilter(String tableName, String filterName, String filterValue, String pageSize, String profile) throws Exception{
		h.help(tableName,"<table-name> <filter-attribute-name> <filter-attribute-value> <page-size> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		util.scanItemByFilter(ddb,tableName,filterName,filterValue, Integer.parseInt(pageSize));
	}
	
	public void ddbScanItemByFilterParallel(String tableName, String filterName, String filterValue, String degreeOfParallel, String profile) throws Exception{
		h.help(tableName,"<table-name> <filter-attribute-name> <filter-attribute-value> <parallel-degree> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		util.scanItemByFilterParallel(ddb,tableName,filterName,filterValue,Integer.parseInt(degreeOfParallel));
	}
	
	public void ddbQueryByHashRangeBeginsWithString(String tableName, String indexName, String hashName, String hashValue, String rangeName, String rangeValue, String ascOrDesc, String profile) throws Exception{
		h.help(tableName,"<table-name> <index-name|null:(Using PK)> <hash-name> <hash-value> <range-name> <range-begins-with-value> <asc|desc> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		if(indexName.equals("null")){indexName=null;}
		util.queryItemByHashStringRangeString(ddb, tableName, hashName, hashValue, rangeName, rangeValue, indexName, ascOrDesc.equals("asc")?true:false);
	}
	
	public void ddbQueryByHashString(String tableName, String indexName, String pkName, String pkValue, String profile) throws Exception{
		h.help(tableName,"<table-name> <indexName|null> <hash-pk-name> <hash-pk-value> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		if(indexName.equals("null")){indexName=null;}
		util.queryItemByHashString(ddb, tableName, pkName, pkValue, indexName);
	}
	
	public void ddbGetItemByPkHashString(String tableName, String pkName, String pkValue, String consistentRead, String profile) throws Exception{
		h.help(tableName,"<table-name> <hash-pk-name> <hash-pk-value> <consistent-read> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		util.getItemByPkHashString(ddb, tableName, pkName, pkValue,consistentRead.equals("true"));
	}

	public void ddbGetItemByPkHashRangeString(String tableName, String pk1Name, String pk1Value, String pk2Name, String pk2Value, String consistentRead, String profile) throws Exception{
		h.help(tableName,"<table-name> <hash-pk-name> <hash-pk-value> <range-pk-name> <range-pk-value> <consistent-read> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		util.getItemByPkHashRangeString(ddb, tableName, pk1Name, pk1Value, pk2Name, pk2Value, consistentRead.equals("true"));
	}
	
	
	public void ddbPutItemStringCrazy(String tableName, String pkHashName, String pkRangeName, String profile) throws Exception{
		h.help(tableName,"<table-name> <hash-pk-name> <range-pk-name> <profile>");
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		String pkRangeNameReal = pkRangeName.equals("null")?null:pkRangeName;
		util.putItemCrazy(ddb, tableName, pkHashName, pkRangeNameReal);
	}
	
	/**
	 * Only for String type attributes.
	 * @param regionTableItemAttributeAndValue 0:profile, 1:tableName
	 * @throws Exception 
	 */
	public void ddbPutItemString(String[] regionTableItemAttributeAndValue) throws Exception{
		//h.help(regionTableItemAttributeAndValue[0],"<region-name> <table-name> <attr-key> <attr-value> ...");
		String profile = regionTableItemAttributeAndValue[0];
		String tableName = regionTableItemAttributeAndValue[1];
		HashMap<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		for(int i=2;i<regionTableItemAttributeAndValue.length;i=i+2){
			item.put(regionTableItemAttributeAndValue[i], new AttributeValue().withS(regionTableItemAttributeAndValue[i+1]));
		}
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		util.putItem(ddb, tableName, item);
	}
	
	public void ddbDeleteItemString(String[] regionTableItemPkAttributeAndValue) throws Exception{
		//h.help(regionTableItemAttributeAndValue[0],"<profile> <table-name> <pk-attr-key> <pk-attr-value> ...");
		String profile = regionTableItemPkAttributeAndValue[0];
		String tableName = regionTableItemPkAttributeAndValue[1];
		HashMap<String, AttributeValue> pk = new HashMap<String, AttributeValue>();
		for(int i=2;i<regionTableItemPkAttributeAndValue.length;i=i+2){
			pk.put(regionTableItemPkAttributeAndValue[i], new AttributeValue().withS(regionTableItemPkAttributeAndValue[i+1]));
		}
		AmazonDynamoDB ddb = (AmazonDynamoDB) Clients.getClientByServiceClassProfile(Clients.DDB, profile);
		DynamodbUtil util = new DynamodbUtil();
		System.out.println("WARNING: deleting dynamodb item...");
		util.deleteItemByPkString(ddb, tableName, pk);		
	}
	
	public void showObjectInBucket(String bucketName, String keyPrefix, String profile) throws Exception{	
		h.help(bucketName,"<bucket> <key-prefix> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		S3Util util = new S3Util();
		if(keyPrefix.equals("null")){
			keyPrefix=null;
		}
		util.listObjectsInBucket(s3, bucketName, keyPrefix);
	}
	
	public void demoEbs() throws Exception{
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, "beijing");
		EC2Util util = new EC2Util();
		List<Instance> bastions = util.getInstancesByNameTagPrefix(ec2, "bastion-beijing");
		for(Instance instance:bastions){
			if(instance.getState().getName().equalsIgnoreCase(InstanceState.RUNNING.toString())){
				System.out.println(instance.getInstanceId());
				boolean attach = true;
				for(InstanceBlockDeviceMapping m:instance.getBlockDeviceMappings()){
					if(m.getDeviceName().equals("/dev/xvdz")){
						attach = false;
						String vid = m.getEbs().getVolumeId();
						ec2.detachVolume(new DetachVolumeRequest().withForce(true)
								.withInstanceId(instance.getInstanceId())
								.withVolumeId(vid));
						System.out.println("Dettaching "+vid+" from /dev/xvdz");
						while(true){
							Thread.sleep(5*1000);
							String state = ec2.describeVolumes(new DescribeVolumesRequest().withVolumeIds(vid)).getVolumes().get(0).getState();
							System.out.println(vid+": "+state.toString());
							if(state.equalsIgnoreCase(VolumeState.Available.toString())){
								ec2.deleteVolume(new DeleteVolumeRequest().withVolumeId(vid));
								System.out.println("Deleting "+vid);
								break;
							}
						}	
					}
				}
				if(attach){
					this.addGp2EbsToEc2(instance.getInstanceId(), "16384", "/dev/xvdz", "beijing");
				}
			}
		}
	}
	
	public void addGp2EbsToEc2(String ec2InstanceId, String size, String device, String profile) throws Exception{
		h.help(ec2InstanceId,"<ec2-instance-id> <vol-size-in-gb> <device> <profile>");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		DescribeInstancesRequest dir = new DescribeInstancesRequest();
		ArrayList<String> ids = new ArrayList<String>(); ids.add(ec2InstanceId);
		dir.setInstanceIds(ids);
		DescribeInstancesResult dirt = ec2.describeInstances(dir);
		List<Reservation> reservations = dirt.getReservations();
		String subnetId = null;
		String az = null;
		if(reservations.size()>0){
			subnetId = reservations.get(0).getInstances().get(0).getSubnetId();
			System.out.println("Instance subnet-id: "+subnetId);
			DescribeSubnetsRequest dsr = new DescribeSubnetsRequest()
											.withSubnetIds(subnetId);
			DescribeSubnetsResult dsrt = ec2.describeSubnets(dsr);
			az = dsrt.getSubnets().get(0).getAvailabilityZone();
			System.out.println("Instance AZ: "+az);
		}
		CreateVolumeRequest cvr = new CreateVolumeRequest()
										.withAvailabilityZone(az)
										.withVolumeType(VolumeType.Gp2)
										.withSize(Integer.parseInt(size));
		
		long startTime = System.currentTimeMillis();
		CreateVolumeResult cvrt = ec2.createVolume(cvr);
		Volume ebs = cvrt.getVolume();
		String volId = ebs.getVolumeId();
		String volAz = ebs.getAvailabilityZone();
		System.out.println("New volume-id: "+volId);
		System.out.println("New volume-zone: "+volAz);
		AttachVolumeRequest avr = new AttachVolumeRequest()
									.withInstanceId(ec2InstanceId)
									.withVolumeId(volId)
									.withDevice(device);
		String state = null;
		DescribeVolumesRequest dvr = new DescribeVolumesRequest().withVolumeIds(volId);
		int c = 1;
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			state = ec2.describeVolumes(dvr).getVolumes().get(0).getState();
			System.out.println(c+++" "+state);
			if(state.equals(VolumeState.Available.toString())){
				break;
			}
		}
		AttachVolumeResult avrt = ec2.attachVolume(avr);
		long ela = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed: "+ela+" (ms)");
		String dev = avrt.getAttachment().getDevice();
		System.out.println("Attach to: "+dev);
	}
	
	public void demoDefaultSdkClientConfig(){
		ClientConfiguration cc = new ClientConfiguration();
		RetryPolicy rp = cc.getRetryPolicy();
		System.out.println(
					"Connection Timeout - Initially: "+cc.getConnectionTimeout()+"\n"+
							"Connection TTL - Expire in Pool: "+cc.getConnectionTTL()+"\n"+
							"Max Connections: "+cc.getMaxConnections()+"\n"+
							"Max Error Retry (5xx): "+cc.getMaxErrorRetry()+"\n"+
							"Protocol: "+cc.getProtocol().name()+"\n"+
							"Retry Policy: "+rp.toString().replaceAll("@.*$","")+"\n"+
							"Retry Policy - Max Error Retry: "+rp.getMaxErrorRetry()+"\n"+
							"Retry Policy - BackoffStrategy: "+rp.getBackoffStrategy().toString().replaceAll("@.*$","")+"\n"+
							"Retry Policy - Honor Max Error Retry (5xx): "+(rp.isMaxErrorRetryInClientConfigHonored()?"YES":"NO")+"\n"+
							"Signer Override: "+cc.getSignerOverride()+"\n"+
							"Socket Buffer Size Hint: (send) "+cc.getSocketBufferSizeHints()[0]+", (receive) "+cc.getSocketBufferSizeHints()[1]+"\n"+
							"Socket Timeout: "+cc.getSocketTimeout()+"\n"+
							"User Agent: "+cc.getUserAgent()
				);
	}
	
	/**
	 * @param fileLocalPath
	 * @param validDay
	 * @return
	 */
	public void shareToPublicChina(String fileLocalPath, String validDurationInDays) throws Exception{
		h.help(fileLocalPath,"<file-local-path> <valid-days>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, "beijing");
		String basename = fileLocalPath.substring(fileLocalPath.lastIndexOf("/"));
		String key = "working-share"+basename;
		System.out.println("Key: "+key);
		PutObjectRequest por = new PutObjectRequest(Config.BACKUP_BUCKET,key,new File(fileLocalPath));
		PutObjectResult pors = s3.putObject(por);
		String etag = pors.getETag();
		System.out.println("ETag: "+etag);
		S3Util s3util = new S3Util();
		URL url = s3util.getPresignedUrl(Config.BACKUP_BUCKET,key,Integer.valueOf(validDurationInDays)*24,"GET", "beijing");
		String presignedUrl = url.toString();
		System.out.println(presignedUrl);
	}
	
	public void generatePresignedUrl(String bucketName, String key, String validDurationInDays, String profile) throws Exception{
		h.help(bucketName,"<bucket-name> <key> <valid-days> <profile>");
		S3Util s3util = new S3Util();
		URL url = null;
		url = s3util.getPresignedUrl(bucketName,key,Integer.valueOf(validDurationInDays)*24,"GET", profile);
		System.out.println(url.toString());
	}
	
	public void generatePresignedMethodUrl(String bucketName, String key, String validDurationInDays, String method, String profile) throws Exception{
		h.help(bucketName,"<bucket-name> <key> <valid-days> <method> <profile>");
		S3Util s3util = new S3Util();
		URL url = null;
		int vd = Integer.parseInt(validDurationInDays);
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		url = s3util.getPresignedUrl(s3, bucketName, key, vd*24, method);
		System.out.println(url.toString());
	}
	
	public void closeAllNetwork() throws Exception{
		this.closeBeijingNetwork();
		this.closeTokyoNetwork();
		this.closeVirginiaNetwork();
	}
	
	/**
	 * shutdown Beijing public network.
	 * @throws Exception
	 */
	public void closeBeijingNetwork() throws Exception{
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, "beijing");
		EC2Util util = new EC2Util();
		util.denyAllIngressOnNACL(ec2, Config.APP_NACL_BEIJING);
		util.denyAllIngressOnNACL(ec2, Config.DEFAULT_NACL_BEIJING);
	}
	
	public void closeVirginiaNetwork() throws Exception{
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, "virginia");
		EC2Util util = new EC2Util();
		util.denyAllIngressOnNACL(ec2, Config.APP_NACL_VIR);
		util.denyAllIngressOnNACL(ec2, Config.DEFAULT_NACL_VIR);
	}
	
	public void closeTokyoNetwork() throws Exception{
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, "tokyo");
		EC2Util util = new EC2Util();
		util.denyAllIngressOnNACL(ec2, Config.APP_NACL_TOKYO);
		util.denyAllIngressOnNACL(ec2, Config.DEFAULT_NACL_TOKYO);
	}
	
	
	public void openBucket(String bucketName, String profile) throws Exception{
		h.help(bucketName,"<bucket> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		Regions region = GeneralUtil.PROFILE_REGIONS.get(profile);
		String partition = "aws";
		for(Regions r:GeneralUtil.CHINA_REGIONS){
			if(r.equals(region)){
				partition="aws-cn";
				break;
			}
		}
		S3Util util = new S3Util();
		String policyJson = "{\n"+
	"\"Version\": \"2012-10-17\",\n"+
	"\"Id\": \"Policy1449411144046\",\n"+
	"\"Statement\": [\n"+
		"{\n"+
			"\"Sid\": \"Stmt144941114259\",\n"+
			"\"Effect\": \"Allow\",\n"+
			"\"Principal\": \"*\",\n"+
			"\"Action\": \"s3:GetObject\",\n"+
			"\"Resource\": \"arn:"+partition+":s3:::"+bucketName+"/*\"\n"+
		"}\n"+
	"]\n"+
	"}";
		System.out.println(policyJson);
		util.setBucketPolicy(s3, bucketName, policyJson);
	}
	
	public void closeBucket(String bucketName, String profile) throws Exception{
		h.help(bucketName,"<bucket> <profile>");
		AmazonS3 s3 = (AmazonS3) Clients.getClientByServiceClassProfile(Clients.S3, profile);
		Regions region = GeneralUtil.PROFILE_REGIONS.get(profile);
		String partition = "aws";
		for(Regions r:GeneralUtil.CHINA_REGIONS){
			if(r.equals(region)){
				partition="aws-cn";
				break;
			}
		}
		S3Util util = new S3Util();
		String policyJson = "{\n"+
	"\"Version\": \"2012-10-17\",\n"+
	"\"Id\": \"Policy1449411144046\",\n"+
	"\"Statement\": [\n"+
		"{\n"+
			"\"Sid\": \"Stmt144941114259\",\n"+
			"\"Effect\": \"Deny\",\n"+
			"\"Principal\": \"*\",\n"+
			"\"Action\": \"s3:GetObject\",\n"+
			"\"Resource\": \"arn:"+partition+":s3:::"+bucketName+"/*\"\n"+
		"}\n"+
	"]\n"+
	"}";
		System.out.println(policyJson);
		util.setBucketPolicy(s3, bucketName, policyJson);
	}
	
	/**
	 * open Beijing public network.
	 * @throws Exception
	 */
	public void openBeijingNetwork() throws Exception{
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, "beijing");
		EC2Util util = new EC2Util();
		util.removeIngressNo49(ec2, Config.APP_NACL_BEIJING);
		util.removeIngressNo49(ec2, Config.DEFAULT_NACL_BEIJING);
	}
	
	public void openVirginiaNetwork() throws Exception{
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, "virginia");
		EC2Util util = new EC2Util();
		util.removeIngressNo49(ec2, Config.APP_NACL_VIR);
		util.removeIngressNo49(ec2, Config.DEFAULT_NACL_VIR);
	}
	
	public void openTokyoNetwork() throws Exception{
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, "tokyo");
		EC2Util util = new EC2Util();
		util.removeIngressNo49(ec2, Config.APP_NACL_TOKYO);
		util.removeIngressNo49(ec2, Config.DEFAULT_NACL_TOKYO);
	}
	
	public void showServiceClass(String serviceAbb) throws Exception{
		h.help(serviceAbb,"<service-name-abbr>");
		System.out.println("\n"+Clients.getServiceClassFromServiceAbbr(serviceAbb)+"\n");
	}
	
	public void showServiceAll() throws IllegalArgumentException, IllegalAccessException{
		int i=1;
		for(String serviceName:SERVICEABB_SERVICENAME.keySet()){
			System.out.println(i+++": "+serviceName+" - "+SERVICEABB_SERVICENAME.get(serviceName));
		}
	}
	
	public static void showRegionByService(String serviceAbb){
		h.help(serviceAbb,"<service-name-abbr>");
		System.out.println("_______\\");
		System.out.println("Service: "+serviceAbb);
		List<Region> regions = RegionUtils.getRegionsForService(serviceAbb);
		int i=0;
		for(Region r:regions){
			System.out.println(++i+") "+r.getName()+" => "+GeneralUtil.REGIONCODE_REGIONNAME.get(r.getName()));
		}
	}
	
	public void showInstanceType() throws Exception{
		int i = 1;
		for (InstanceType type : InstanceType.values()){
			System.out.println(i+++") "+ type.toString());
		}
	}
	
	public String base64Encode(String plain){
		h.help(plain,"<plain-text>");
		byte[] b = null;
		String base64 = null;
		try {
			b=plain.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(b!=null){
			base64 = new BASE64Encoder().encode(b);
		}
		System.out.println(base64);
		return base64;
	}
	
	public String base64Decode(String base64){
		h.help(base64,"<base64-text>");
		byte[] b = null;
		String plain = null;
		if(base64 != null){
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(base64);
				plain = new String(b, "utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(plain);
		return plain;
	}
	
	public String base64EncodeFromFile(String file){
		h.help(file,"<local-file-path>");
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(file)));
			line = br.readLine();
			while(line!=null){
				sb.append(line+"\n");
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String script = new String(sb);
		System.out.println("== File Content:");
		System.out.println(script);
		if(script.equals("")){
			throw new RuntimeException("File Content is NULL!!");
		}
		String base64 = this.base64Encode(script);
		return base64;
	}
	
	public void changeAsgAmiSoft(String asgName, String ami, String rollingWaitMins, String profile) throws Exception{
		h.help(asgName,"<asg-name> <ami-id> <rolling-terminate-wait-minutes> <profile>");
		AutoscalingUtil util = new AutoscalingUtil();
		AmazonAutoScaling aas = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
		util.changeAMIForAsg(aas, asgName, ami, false, Integer.parseInt(rollingWaitMins));
	}
	
	public void changeAsgInstaneTypeSoft(String asgName, String instanceType, String rollingWaitMins, String profile) throws Exception{
		h.help(asgName,"<asg-name> <instane-type> <rolling-terminate-wait-minutes> <profile>");
		AutoscalingUtil util = new AutoscalingUtil();
		AmazonAutoScaling aas = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
		util.changeInstanceTypeForAsg(aas, asgName, instanceType, false, Integer.parseInt(rollingWaitMins));
	}
	
	public void changeAsgUserdataSoft(String asgName, String userdataFilePath, String rollingWaitMins, String profile) throws Exception{
		h.help(asgName,"<asg-name> <userdata-file-path> <rolling-terminate-wait-minutes> <profile>");
		AutoscalingUtil util = new AutoscalingUtil();
		AmazonAutoScaling aas = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
		util.changeUserdataForAsg(aas, asgName, this.base64EncodeFromFile(userdataFilePath), false, Integer.parseInt(rollingWaitMins));
	}
	
	public void changeAsgLcSoft(String asgName, String lcName, String rollingWaitMins, String profile) throws Exception{
		h.help(asgName,"<asg-name> <launch-config-name> <rolling-terminate-wait-minutes> <profile>");
		AutoscalingUtil util = new AutoscalingUtil();
		AmazonAutoScaling aas = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
		util.changeLaunchConfigurationForAsg(aas, asgName, lcName, false, Integer.parseInt(rollingWaitMins));
	}
	
	public void swapAsgLcSoft(String asgName, String rollingWaitMins, String profile) throws Exception{
		h.help(asgName,"<asg-name> <rolling-terminate-wait-minutes> <profile>");
		AutoscalingUtil util = new AutoscalingUtil();
		AmazonAutoScaling aas = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
		util.swapLaunchConfigurationForAsg(aas, asgName, false, Integer.parseInt(rollingWaitMins));
	}
	
	public void showAsgLc(String asgName, String profile) throws Exception{
		h.help(asgName,"<asg-name> <profile>");
		AutoscalingUtil util = new AutoscalingUtil();
		AmazonAutoScaling aas = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
		util.printLaunchConfigurationForAsg(aas, asgName);
	}
	
	/**
	 * This routine will eventually create new LC with a version increasing name.
	 * So, if the new name is not available, abort the mission. 
	 * @param asgName
	 * @param waitMins
	 * @param commands
	 * @throws Exception
	 */
	public static void commandsToAmiAsgSoftBeijing(String... commands) throws Exception{
		//h.help(asgName,"<asg-name> <wait-mins-during-swap-members>, <command1, command2, ...>");
		AutoscalingUtil au = new AutoscalingUtil();
		AmazonAutoScaling aas = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, "beijing");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, "beijing");
		if(au.checkNewLaunchConfigAvail(aas, commands[0])){
			if(commands[2].startsWith("file://")){
				au.commandsToAmiForAsgByName(aas, ec2, Config.BEIJING_EC2_KEYPAIR_NAME, commands[0], Config.BEIJING_DEFAULT_VPC_SUBNET1, Config.BEIJING_DEFAULT_VPC_SUBNET2, Config.BEIJING_DEFAULT_VPC_ALLOWALL_SECURITY_GROUP, new File(commands[2].replaceFirst("file://","")), Integer.parseInt(commands[1]), "commandsToAmi-"+commands[0], false);
			}
			else{
				au.commandsToAmiForAsgByName(aas, ec2, Config.BEIJING_EC2_KEYPAIR_NAME, commands[0], Config.BEIJING_DEFAULT_VPC_SUBNET1, Config.BEIJING_DEFAULT_VPC_SUBNET2, Config.BEIJING_DEFAULT_VPC_ALLOWALL_SECURITY_GROUP, commands, Integer.parseInt(commands[1]), "commandsToAmi-"+commands[0], false);
			}
		}
		else{
			System.out.println("Next launch configuration name is occupied, please have a check.");
		}
	}
	
	public void diffLc(String lc1, String lc2, String profile) throws Exception{
		h.help(lc1,"<launch-config-#1> <launch-config-#2> <profile>");
		AutoscalingUtil au = new AutoscalingUtil();
		AmazonAutoScaling aas = (AmazonAutoScaling) Clients.getClientByServiceClassProfile(Clients.ASG, profile);
		Map<String,String> diff = au.getLaunchConfigSideBySide(aas, lc1, lc2);
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(diff.keySet());
		Collections.sort(keys);
		String[] compareArray = null;
		String[] userdataArray = new String[2];
		for(String key:keys){
			if(key.startsWith("UserData for ")){
				if(userdataArray[0]==null){
					userdataArray[0] = diff.get(key);
				}
				else{
					userdataArray[1] = diff.get(key);
					if( !userdataArray[0].equals(userdataArray[1])){
						System.out.println("("+key+")\t"+userdataArray[1]);
						System.out.println("(Other)\t"+userdataArray[0]);
					}
				}
			}
			else{
				compareArray = diff.get(key).split(":");
				if(compareArray!=null && compareArray.length==2 && !compareArray[0].equals(compareArray[1])){
					System.out.println("("+key+")\t"+diff.get(key));
				}
			}
		}
	}
	
	public void showSgInSourceBySg(String sgid, String profile) throws Exception{
		h.help(sgid,"<security-group-id> <profile>");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		for(String sid:util.getGroupIdsInSources(ec2, sgid)){
			System.out.println(sid+": "+ec2.describeSecurityGroups(new DescribeSecurityGroupsRequest().withGroupIds(sid)).getSecurityGroups().get(0).getGroupName());
		}
	}
	
	public void showSgWhoIsUsingMe(String sgid, String profile) throws Exception{
		h.help(sgid,"<security-group-id> <profile>");
		AmazonEC2 ec2 = (AmazonEC2) Clients.getClientByServiceClassProfile(Clients.EC2, profile);
		EC2Util util = new EC2Util();
		for(String sid:util.getReferencingResourceIds(ec2, sgid)){
			if(sid.startsWith("sg-")){
				System.out.println(sid+": "+ec2.describeSecurityGroups(new DescribeSecurityGroupsRequest().withGroupIds(sid)).getSecurityGroups().get(0).getGroupName());
			}
			else{
				System.out.println(sid);
			}
		}
	}

	
	public static void coreV2(String[] args) throws Exception{
		
		if(args==null || args.length==0){
			System.out.println("Usage:");
			TreeSet<String> ts = new TreeSet<String>();
			StringBuffer sb = null;
			Method[] allMethods = Biu.class.getDeclaredMethods();
			ArrayList<Method> methods = new ArrayList<Method>();
			for(Method m:allMethods){
				if(Modifier.isPublic(m.getModifiers())){
					methods.add(m);
				}
			}
			String mn = null;
			for(Method m:methods){
				sb = new StringBuffer();
				mn = m.getName();
				if(SKIPPED_METHODS.contains(mn)){
					continue;
				}
				sb.append(mn+": ");
				int parameterCount = m.getParameterTypes().length;
				for(int i=0;i<parameterCount;i++){
					sb.append("[]");
				}
				ts.add(new String(sb));
			}
			for(String s:ts){
				System.out.println(s);
			}
			return;
		}
		
		Biu u = new Biu();
		Class<?> clazz = u.getClass();
		Method[] allMethods = clazz.getDeclaredMethods();
		ArrayList<Method> methods = new ArrayList<Method>();
		for(Method m:allMethods){
			if(Modifier.isPublic(m.getModifiers())){
				methods.add(m);
			}
		}
		if(SKIPPED_METHODS.contains(args[0])){
			//System.out.println("options unkown.");
			return;
		}
		for(Method m:methods){
			if (m.getName().equals(args[0])){
				//System.out.println(m.getName()+" :: "+args[0]);
				// Pass through #1: putItemToDdb, #2: deleteItemFromDdb
				if(args[0].equals("ddbDeleteItemString")){
					String[] parameters = Arrays.copyOfRange(args, 1, args.length);
					if(parameters[0].equals("-h")){
						System.out.println("<profile> <table-name> <pk-attr-name> <pk-attr-value> ...");
						return;
					}
					u.ddbDeleteItemString(parameters);
					return;
				}
				if(args[0].equals("ddbPutItemString")){
					String[] parameters = Arrays.copyOfRange(args, 1, args.length);
					if(parameters[0].equals("-h")){
						System.out.println("<profile> <table-name> <attr-key-name> <attr-value> ...");
						return;
					}
					u.ddbPutItemString(parameters);
					return;
				}
				if(args[0].equals("commandsToAmiAsgSoftBeijing")){
					String[] parameters = Arrays.copyOfRange(args, 1, args.length);
					if(parameters[0].equals("-h")){
						System.out.println("<asg-name> <wait-mins-during-swapping> <file:///path-to-script-file> | \\\"<command1>\\\", \\\"<command2>\\\", \\\"<...>\\\"");
						return;
					}
					commandsToAmiAsgSoftBeijing(parameters);
					return;
				}
				// Options filter
				Class<?>[] paramTypes = m.getParameterTypes();
				if(paramTypes==null || paramTypes.length==0 || paramTypes[0]!=(new String[]{"XXX"}.getClass())){
					int paramCount = paramTypes.length;
					String[] paramValues = new String[paramCount];
					// Take '-h' help into consideration.
					for(int i=0;i<paramValues.length;i++){
						if(args[1].equals("-h")){
							paramValues[0] = args[1];
							for(int j=1;j<paramValues.length;j++){
								paramValues[j] = null;
							}
							break;
						}
						else{
							paramValues[i] = args[i+1];
						}
					}
					m.invoke(u, (Object[])paramValues);
				}
				else{
					String[] mParameters = Arrays.copyOfRange(args, 1, args.length);
					System.out.println(m.getName()+": "+Arrays.toString(mParameters));
					m.invoke(u, (Object[])(mParameters));
				}
				return;
			}
		}
		
		Helper.searh(args[0]);
		
		return;
	}
	
	public static void main(String[] args) throws Exception{
		coreV2(args);
	}
}
