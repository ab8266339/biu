package bglutil.common;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import sun.misc.BASE64Encoder;
import bglutil.common.types.DirectoryServiceWorkspacesClients;
import bglutil.common.types.GLHashMap;
import bglutil.common.types.GLTreeMap;
import bglutil.conf.Config;
import bglutil.main.Biu;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudhsm.AWSCloudHSM;
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearch;
import com.amazonaws.services.cloudtrail.AWSCloudTrail;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.codecommit.AWSCodeCommit;
import com.amazonaws.services.codedeploy.AmazonCodeDeploy;
import com.amazonaws.services.codedeploy.AmazonCodeDeployClient;
import com.amazonaws.services.codepipeline.AWSCodePipeline;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitosync.AmazonCognitoSync;
import com.amazonaws.services.config.AmazonConfig;
import com.amazonaws.services.config.AmazonConfigClient;
import com.amazonaws.services.databasemigrationservice.AWSDatabaseMigrationService;
import com.amazonaws.services.datapipeline.DataPipeline;
import com.amazonaws.services.datapipeline.DataPipelineClient;
import com.amazonaws.services.devicefarm.AWSDeviceFarm;
import com.amazonaws.services.directconnect.AmazonDirectConnect;
import com.amazonaws.services.directory.AWSDirectoryService;
import com.amazonaws.services.directory.AWSDirectoryServiceClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClient;
import com.amazonaws.services.elasticache.AmazonElastiCache;
import com.amazonaws.services.elasticache.AmazonElastiCacheClient;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk;
import com.amazonaws.services.elasticfilesystem.AmazonElasticFileSystem;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.gamelift.AmazonGameLift;
import com.amazonaws.services.glacier.AmazonGlacier;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.importexport.AmazonImportExport;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iotdata.AWSIotData;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClient;
import com.amazonaws.services.machinelearning.AmazonMachineLearning;
import com.amazonaws.services.opsworks.AWSOpsWorks;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.redshift.AmazonRedshift;
import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53domains.AmazonRoute53Domains;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.storagegateway.AWSStorageGateway;
import com.amazonaws.services.support.AWSSupport;
import com.amazonaws.services.waf.AWSWAF;
import com.amazonaws.services.workspaces.AmazonWorkspaces;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

public class GeneralUtil {
	
	private static Helper h = new Helper();

	public static final GLHashMap<String, String> REGIONCODE_REGIONNAME = new GLHashMap<String, String>();
	public static final GLHashMap<String, Regions> PROFILE_REGIONS = new GLHashMap<String, Regions>();

	public static final GLTreeMap<String, String> SERVICEABB_SERVICENAME_V2 = new GLTreeMap<String, String>();
	public static final GLHashMap<String, String> SERVICENAME_PACKAGECLASS = new GLHashMap<String, String>();
	public static final GLHashMap<String, String> SERVICENAME_PACKAGECLASS_FULL = new GLHashMap<String, String>();

	public static final String[] SHOW_SERVICE_PROFILES = new String[] { "beijing", "seoul", "mumbai", "singapore",
			"california", "ireland", "frankfurt", "sydney", "sanpaulo", "tokyo", "virginia", "oregon" };
	public static final String[] ALL_PROFILES = SHOW_SERVICE_PROFILES;
	public static final Regions[] GLOBAL_REGIONS = new Regions[] { Regions.US_EAST_1, Regions.AP_NORTHEAST_1,
			Regions.AP_SOUTHEAST_1, Regions.AP_SOUTHEAST_2, Regions.AP_SOUTH_1, Regions.SA_EAST_1, Regions.US_WEST_1,
			Regions.US_WEST_2, Regions.EU_WEST_1, Regions.EU_CENTRAL_1, Regions.AP_NORTHEAST_2 };
	public static final Regions[] CHINA_REGIONS = new Regions[] { Regions.CN_NORTH_1 };
	public static final Regions[] ALL_REGIONS = new Regions[] { Regions.CN_NORTH_1, Regions.US_EAST_1,
			Regions.AP_NORTHEAST_1, Regions.AP_SOUTHEAST_1, Regions.AP_SOUTHEAST_2, Regions.AP_SOUTH_1,
			Regions.SA_EAST_1, Regions.US_WEST_1, Regions.US_WEST_2, Regions.EU_WEST_1, Regions.EU_CENTRAL_1,
			Regions.AP_NORTHEAST_2 };

	static {

		REGIONCODE_REGIONNAME.put("cn-north-1", "Beijing").put("us-east-1", "N. Virginia").put("us-west-2", "Oregon")
				.put("us-west-1", "N. California").put("eu-west-1", "Ireland").put("eu-central-1", "Frankfurt")
				.put("ap-southeast-1", "Singapore").put("ap-northeast-1", "Tokyo").put("ap-northeast-2", "Seoul")
				.put("ap-southeast-2", "Sydney").put("ap-south-1", "Mumbai").put("sa-east-1", "São Paulo")
				.put("us-gov-west-1", "US. Gov");

		PROFILE_REGIONS.put("default", Regions.CN_NORTH_1).put("global", Regions.US_EAST_1)
				.put("china", Regions.CN_NORTH_1).put(Config.MFA_USERNAME, Regions.US_EAST_1)
				.put("beijing", Regions.CN_NORTH_1).put("virginia", Regions.US_EAST_1)
				.put("tokyo", Regions.AP_NORTHEAST_1).put("seoul", Regions.AP_NORTHEAST_2)
				.put("singapore", Regions.AP_SOUTHEAST_1).put("oregon", Regions.US_WEST_2)
				.put("california", Regions.US_WEST_1).put("ireland", Regions.EU_WEST_1)
				.put("frankfurt", Regions.EU_CENTRAL_1).put("sydney", Regions.AP_SOUTHEAST_2)
				.put("sanpaulo", Regions.SA_EAST_1).put("mumbai", Regions.AP_SOUTH_1);

		SERVICEABB_SERVICENAME_V2
				.put(AmazonAutoScaling.ENDPOINT_PREFIX, "Autoscaling")
				.put(AmazonCloudFormation.ENDPOINT_PREFIX, "CloudFormation")
				.put(AmazonCloudFront.ENDPOINT_PREFIX, "CloudFront")
				.put(AWSCloudHSM.ENDPOINT_PREFIX, "CloudHSM")
				.put(AmazonCloudSearch.ENDPOINT_PREFIX, "CloudSearch")
				.put(AWSCloudTrail.ENDPOINT_PREFIX, "CloudTrail")
				.put(AWSCodeCommit.ENDPOINT_PREFIX, "CodeCommit")
				.put(AmazonCodeDeploy.ENDPOINT_PREFIX, "CodeDeploy")
				.put(AWSCodePipeline.ENDPOINT_PREFIX, "CodePipeline")
				.put(AmazonCognitoIdentity.ENDPOINT_PREFIX, "CognitoIdentity")
				.put(AWSCognitoIdentityProvider.ENDPOINT_PREFIX, "CognitoIdentityProvider")
				.put(AmazonCognitoSync.ENDPOINT_PREFIX, "CognitoSync")
				.put(AmazonConfig.ENDPOINT_PREFIX, "Config")
				.put(AWSIotData.ENDPOINT_PREFIX, "IoTData")
				.put(DataPipeline.ENDPOINT_PREFIX, "DataPipeline")
				.put(AWSDeviceFarm.ENDPOINT_PREFIX, "DeviceFarm")
				.put(AmazonDirectConnect.ENDPOINT_PREFIX, "DirectConnect")
				.put(AWSDatabaseMigrationService.ENDPOINT_PREFIX, "DMS")
				.put(AWSDirectoryService.ENDPOINT_PREFIX, "Directory")
				.put(AmazonDynamoDB.ENDPOINT_PREFIX, "Dynamodb")
				.put(AmazonEC2.ENDPOINT_PREFIX, "EC2")
				.put(AmazonECS.ENDPOINT_PREFIX, "EC2ContainerService")
				.put(AmazonElastiCache.ENDPOINT_PREFIX, "Elasticache")
				.put(AWSElasticBeanstalk.ENDPOINT_PREFIX, "ElasticBeanstalk")
				.put(AmazonElasticFileSystem.ENDPOINT_PREFIX, "ElasticFileSystem")
				.put(AmazonElasticLoadBalancing.ENDPOINT_PREFIX, "ElasticLoadbalancing")
				.put(AmazonElasticMapReduce.ENDPOINT_PREFIX, "ElasticMapReduce")
				.put(AmazonElasticTranscoder.ENDPOINT_PREFIX, "ElasticTranscoder")
				.put(AmazonSimpleEmailService.ENDPOINT_PREFIX, "Email")
				.put(AmazonGameLift.ENDPOINT_PREFIX, "GameLift")
				.put(AmazonGlacier.ENDPOINT_PREFIX, "Glacier")
				.put(AmazonIdentityManagement.ENDPOINT_PREFIX, "IAM")
				.put(AmazonImportExport.ENDPOINT_PREFIX, "ImportExport")
				.put(AWSIot.ENDPOINT_PREFIX, "IoT")
				.put(AmazonKinesis.ENDPOINT_PREFIX, "Kinesis")
				.put(AWSKMS.ENDPOINT_PREFIX, "KeyManagementService")
				.put(AWSLambda.ENDPOINT_PREFIX, "Lambda")
				.put(AWSLogs.ENDPOINT_PREFIX, "CloudWatchLogs")
				.put(AmazonMachineLearning.ENDPOINT_PREFIX, "MachineLearning")
				.put(AmazonCloudWatch.ENDPOINT_PREFIX, "CloudWatch")
				.put(AWSOpsWorks.ENDPOINT_PREFIX, "Opsworks")
				.put(AmazonRDS.ENDPOINT_PREFIX, "RDS")
				.put(AmazonRedshift.ENDPOINT_PREFIX, "RedShift")
				.put(AmazonRoute53.ENDPOINT_PREFIX, "Route53")
				.put(AmazonRoute53Domains.ENDPOINT_PREFIX, "Route53Domains")
				.put(AmazonS3.ENDPOINT_PREFIX, "S3")
				.put(AmazonSimpleDB.ENDPOINT_PREFIX, "SimpleDB")
				.put(AmazonSNS.ENDPOINT_PREFIX, "SNS")
				.put(AmazonSQS.ENDPOINT_PREFIX, "SQS")
				.put(AWSSimpleSystemsManagement.ENDPOINT_PREFIX, "EC2SimpleSystemsManager")
				.put(AWSStorageGateway.ENDPOINT_PREFIX, "StorageGateway")
				.put(AmazonDynamoDBStreams.ENDPOINT_PREFIX, "DynamodbStreams")
				.put(AWSSecurityTokenService.ENDPOINT_PREFIX, "STS")
				.put(AWSSupport.ENDPOINT_PREFIX, "Support")
				.put(AmazonSimpleWorkflow.ENDPOINT_PREFIX, "SimpleWorkflow")
				.put(AWSWAF.ENDPOINT_PREFIX, "WAF")
				.put(AmazonWorkspaces.ENDPOINT_PREFIX, "Workspaces");
		
		/**
		 * Target: Be sync with SERVICENAME_PACKAGECLASS_FULL
		 */
		SERVICENAME_PACKAGECLASS
				.put("Autoscaling", Clients.ASG)
				.put("CloudFormation", Clients.CFN)
				.put("CloudFront", Clients.CF)
				.put("CloudHSM", Clients.HSM)
				.put("CloudSearch", Clients.CLOUDSEARCH)
				.put("CloudTrail", Clients.CLOUDTRAIL)
				.put("CodeCommit", Clients.CODECOMMIT)
				.put("CodeDeploy", Clients.CODEDEPLOY)
				//TODO .put("CodePipeline",Clients.CODEPIPELINE)
				//TODO .put("CognitoIdentity", Clients.COGNITOID)
				//TODO .put("CognitoSync", Clients.COGNITOSYNC)
				//TODO .put("CognitoIdentityProvider",Clients.COGNITOIDP)
				.put("Config", Clients.CONFIG)
				//TODO .put("IoTData", Clients.IOTDATA)
				.put("DataPipeline", Clients.DATAPIPELINE)
				//TODO .put("DeviceFarm",Clients.DEVICEFARM)
				//TODO .put("DirectConnect", Clients.DX)
				//TODO .put("DMS", Clients.DMS)
				.put("Directory", Clients.DS)
				.put("Dynamodb", Clients.DDB)
				.put("EC2", Clients.EC2)
				.put("EC2ContainerService", Clients.ECS)
				.put("Elasticache", Clients.ELASTICACHE)
				.put("ElasticBeanstalk", Clients.EB)
				//TODO .put("ElasticFileSystem", Clients.EFS)
				.put("ElasticLoadbalancing", Clients.ELB)
				.put("ElasticMapReduce", Clients.EMR)
				//TODO .put("ElasticTranscoder", Clients.TRANSCODER)
				//TODO .put("Email", Clients.SES)
				//TODO .put("GameLift",Clients.GAMELIFT)
				.put("Glacier", Clients.GLACIER)
				.put("IAM", Clients.IAM)
				//TODO .put("ImportExport",Clients.IMPORTEXPORT)
				//TODO .put("IoT", Clients.IOT)
				.put("Kinesis", Clients.KINESIS)
				.put("KeyManagementService", Clients.KMS)
				.put("Lambda", Clients.LAMBDA)
				.put("CloudWatchLogs", Clients.LOGS)
				//TODO .put("MachineLearning", Clients.ML)
				.put("CloudWatch", Clients.CW)
				//TODO .put("Opsworks", Clients.OPSWORKS)
				.put("RDS", Clients.RDS)
				//TODO .put("RedShift", Clients.REDSHIFT)
				//TODO .put("Route53", Clients.R53)
				//TODO .put("Route53Domains",Clients.R53DOMAIN)
				.put("S3", Clients.S3)
				//TODO .put("SimpleDB",Clients.SDB)
				.put("SNS", Clients.SNS)
				.put("SQS", Clients.SQS)
				//TODO .put("EC2SimpleSystemsManager",Clients.EC2SSM)
				//TODO .put("StorageGateway", Clients.SGW)
				//TODO .put("DynamodbStreams",Clients.DDBSTREAMS)
				.put("STS", Clients.STS)
				//TODO .put("Support", Clients.SUPPORT)
				//TODO .put("SimpleWorkflow", Clients.SWF)
				//TODO .put("WAF", Clients.WAF)
				.put("Workspaces", Clients.WORKSPACES)
				;
		
		SERVICENAME_PACKAGECLASS_FULL
		.put("Autoscaling", Clients.ASG)
		.put("CloudFormation", Clients.CFN)
		.put("CloudFront", Clients.CF)
		.put("CloudHSM", Clients.HSM)
		.put("CloudSearch", Clients.CLOUDSEARCH)
		.put("CloudTrail", Clients.CLOUDTRAIL)
		.put("CodeCommit", Clients.CODECOMMIT)
		.put("CodeDeploy", Clients.CODEDEPLOY)
		.put("CodePipeline",Clients.CODEPIPELINE)
		.put("CognitoIdentity", Clients.COGNITOID)
		.put("CognitoSync", Clients.COGNITOSYNC)
		.put("CognitoIdentityProvider",Clients.COGNITOIDP)
		.put("Config", Clients.CONFIG)
		.put("IoTData", Clients.IOTDATA)
		.put("DataPipeline", Clients.DATAPIPELINE)
		.put("DeviceFarm",Clients.DEVICEFARM)
		.put("DirectConnect", Clients.DX)
		.put("DMS", Clients.DMS)
		.put("Directory", Clients.DS)
		.put("Dynamodb", Clients.DDB)
		.put("EC2", Clients.EC2)
		.put("EC2ContainerService", Clients.ECS)
		.put("Elasticache", Clients.ELASTICACHE)
		.put("ElasticBeanstalk", Clients.EB)
		.put("ElasticFileSystem", Clients.EFS)
		.put("ElasticLoadbalancing", Clients.ELB)
		.put("ElasticMapReduce", Clients.EMR)
		.put("ElasticTranscoder", Clients.TRANSCODER)
		.put("Email", Clients.SES)
		.put("GameLift",Clients.GAMELIFT)
		.put("Glacier", Clients.GLACIER)
		.put("IAM", Clients.IAM)
		.put("ImportExport",Clients.IMPORTEXPORT)
		.put("IoT", Clients.IOT)
		.put("Kinesis", Clients.KINESIS)
		.put("KeyManagementService", Clients.KMS)
		.put("Lambda", Clients.LAMBDA)
		.put("CloudWatchLogs", Clients.LOGS)
		.put("MachineLearning", Clients.ML)
		.put("CloudWatch", Clients.CW)
		.put("Opsworks", Clients.OPSWORKS)
		.put("RDS", Clients.RDS)
		.put("RedShift", Clients.REDSHIFT)
		.put("Route53", Clients.R53)
		.put("Route53Domains",Clients.R53DOMAIN)
		.put("S3", Clients.S3)
		.put("SimpleDB",Clients.SDB)
		.put("SNS", Clients.SNS)
		.put("SQS", Clients.SQS)
		.put("EC2SimpleSystemsManager",Clients.EC2SSM)
		.put("StorageGateway", Clients.SGW)
		.put("DynamodbStreams",Clients.DDBSTREAMS)
		.put("STS", Clients.STS)
		.put("Support", Clients.SUPPORT)
		.put("SimpleWorkflow", Clients.SWF)
		.put("WAF", Clients.WAF)
		.put("Workspaces", Clients.WORKSPACES);
		
	}

	public static JsonNode getJsonNode(String jsonString) {
		String prettyJson = 
				jsonString.replaceAll("\"\\{", "{")
				.replaceAll("\\}\"", "}")
				.replaceAll(" {1,}:", ":");
		return Jackson.jsonNodeOf(prettyJson);
	}
	
	public static String getArn(String service, String resourceType, String resourceName, String resourceDiscriminator,
			String profile) throws Exception {
		if (!Biu.SERVICEABB_SERVICENAME.containsKey(service)) {
			throw new Exception(service + " is not in Biu.SERVICEABB_SERVICENAME");
		}
		String prefix = "arn:" + (GeneralUtil.isChinaProfile(profile) ? "aws-cn:" : "aws:") + service + ":";
		String regionName = null;
		String accountId = null;
		String suffix = null;
		switch (service) {
		case "sts":
		case "iam":
		case "s3":
			regionName = ":";
			break;
		default:
			regionName = Clients.getRegionCode(profile) + ":";
			break;
		}

		AWSSecurityTokenService sts = (AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS,
				profile);
		switch (service) {
		case "s3":
			accountId = ":";
			break;
		case "iam":
			accountId = (resourceType.equalsIgnoreCase("managed-policy")) ? "aws:"
					: (new STSUtil().getAccountId(sts) + ":");
			break;
		default:
			accountId = new STSUtil().getAccountId(sts) + ":";
			break;
		}

		switch (resourceType) {
		case "master-account":
			suffix = "root";
			break;
		case "user":
			suffix = "user/" + resourceName;
			break;
		case "group":
			suffix = "group/" + resourceName;
			break;
		case "assumed-role":
			suffix = "assumed-role/" + resourceName + "/" + resourceDiscriminator;
			break;
		case "role":
			suffix = "role/" + resourceName;
			break;
		case "managed-policy":
		case "policy":
			suffix = "policy/" + resourceName;
			break;
		case "repo":
			suffix = resourceName;
			break;
		case "topic":
			suffix = resourceName;
			break;
		case "object":
			suffix = resourceName;
			break;
		case "instance":
			suffix = "instance/" + resourceName;
			break;
		case "key-pair":
			suffix = "key-pair/" + resourceName;
			break;
		case "eni":
			suffix = "network-interface/" + resourceName;
			break;
		case "sg":
			suffix = "security-group/" + resourceName;
			break;
		case "subnet":
			suffix = "subnet/" + resourceName;
			break;
		case "volume":
			suffix = "volume/" + resourceName;
			break;
		case "ami":
			suffix = "image/" + resourceName;
			break;
		}
		return prefix + regionName + accountId + suffix;
	}

	public static boolean isCompatible(String serviceAbbr, String profile) {
		List<Region> regions = RegionUtils.getRegionsForService(serviceAbbr);
		Region region = Region.getRegion(PROFILE_REGIONS.get(profile));
		// Fix the bug in AWS SDK
		if(serviceAbbr.equals(AWSLogs.ENDPOINT_PREFIX) && isChinaProfile(profile)){
			regions.add(region);
		}
		else if(serviceAbbr.equals(AmazonRedshift.ENDPOINT_PREFIX) && isChinaProfile(profile)){
			regions.add(region);
		}
		return regions.contains(region);
	}

	public static boolean isGlobalService(String serviceName) {
		boolean ret = false;
		switch (serviceName) {
		case AmazonCloudFront.ENDPOINT_PREFIX:
			ret = true;
			break;
		case AmazonRoute53.ENDPOINT_PREFIX:
			ret = true;
			break;
		}
		return ret;
	}

	public static boolean isChinaProfile(String profile) {
		return Clients.isChinaRegion(PROFILE_REGIONS.get(profile));
	}

	public static String base64Encode(String plain) {
		byte[] b = null;
		String base64 = null;
		try {
			b = plain.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			base64 = new BASE64Encoder().encode(b);
		}
		return base64;
	}

	public static String versionNumberIncrease(String currentName) {
		String versionPart = currentName.substring(currentName.lastIndexOf("-v"));
		String currentVersionNumber = versionPart.replace("-v", "");
		String nextVersionNumber = String.valueOf(Integer.valueOf(currentVersionNumber) + 1);
		System.out.println("Next version number is: #" + nextVersionNumber);
		String newName = currentName.replace("-v" + currentVersionNumber, "-v" + nextVersionNumber);
		return newName;
	}
	
	public static void tagResource(AmazonEC2 ec2, String resourceId, String tagKey, String tagValue){
		Tag tag = new Tag().withKey(tagKey).withValue(tagValue);
		ec2.createTags(new CreateTagsRequest().withResources(resourceId).withTags(tag));
	}

	public static void showAllGlobalResource() throws Exception {
		System.out.println("\n############## Global #############");
		AmazonIdentityManagement iam = (AmazonIdentityManagement) Clients.getClientByServiceClassProfile(Clients.IAM,
				"virginia");
		IAMUtil util = new IAMUtil();
		util.printAllPhysicalId(iam);
	}

	private static void showCheckingSection(String section) {
		System.out.println("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.out.println("# Checking " + section + "...");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}

	public static void showAllResourceInProfileV2(String profile) throws Exception {
		h.title("\n############## " + Clients.getRegionCode(profile) + " ("
				+ REGIONCODE_REGIONNAME.get(Clients.getRegionCode(profile)) + ") #############");
		TreeSet<String> serviceAbbs = new TreeSet<String>(SERVICEABB_SERVICENAME_V2.keySet());
		for (String serviceAbb : serviceAbbs) {
			if (!GeneralUtil.isCompatible(serviceAbb, profile)
				||GeneralUtil.SERVICENAME_PACKAGECLASS.get(SERVICEABB_SERVICENAME_V2.get(serviceAbb))==null) {
				continue;
			}
			showCheckingSection(SERVICEABB_SERVICENAME_V2.get(serviceAbb));
			IUtil util = Clients.getUtilByServiceAbbr(serviceAbb);
			Object client = null;
			if(serviceAbb.equals(AmazonWorkspaces.ENDPOINT_PREFIX)){
				AmazonWorkspaces ws = (AmazonWorkspaces) Clients.getClientByServiceAbbrProfileProd(serviceAbb, profile);
				AWSDirectoryService ds = (AWSDirectoryService) Clients.getClientByServiceAbbrProfileProd(AWSDirectoryService.ENDPOINT_PREFIX, profile);
				client = new DirectoryServiceWorkspacesClients(ds,ws);
			}else{
				client = Clients.getClientByServiceAbbrProfileProd(serviceAbb, profile);
			}
			util.printAllPhysicalId(client);
		}
	}

	public static void showAllResource() throws Exception {
		for (String profile : SHOW_SERVICE_PROFILES) {
			showAllResourceInProfileV2(profile);
		}
		showAllGlobalResource();
	}
}
