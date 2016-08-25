package bglutil.main;

import bglutil.common.GlacierUtil;
import bglutil.common.SNSUtil;
import bglutil.common.types.GlacierInventoryRetrievalParameters;
import bglutil.common.types.SNSNotificationMessageGlacierJobRetrieveInventory;
import bglutil.common.types.SNSNotification;

import com.amazonaws.util.json.Jackson;

public class TestBiu {
	
	private static String[] base64Encode = new String[]{"base64Encode","glacier"};
	private static String[] base64Decode = new String[]{"base64Decode","Z2xhY2llcg=="};
	private static String[] commandsToAmiAsgSoftBeijingOptions = new String[]{"beijing-bastion-asg-v2", "5",
															"\"command0","options0\"",
															"\"command1 options1\"", "\"command3\"", "\"command2 options2\"",
															"\"command7", "option71", "option72\""};
	
	public static void main(String[] args) throws Exception{
		//Biu.coreV2(base64Encode);
		//Biu.coreV2(base64Decode);
		//Biu.commandsToAmiAsgSoftBeijing(commandsToAmiAsgSoftBeijingOptions);
		String j="{\"Type\" : \"Notification\",\"MessageId\" : \"c800301d-2b50-5d54-aed9-603bfd43b6a1\",\"TopicArn\" : \"arn:aws-cn:sns:cn-north-1:296259413523:gp2-topic\",\"Message\":\"{\"Action\":\"InventoryRetrieval\",\"ArchiveId\":null,\"ArchiveSHA256TreeHash\":null,\"ArchiveSizeInBytes\":null,\"Completed\":true,\"CompletionDate\":\"2016-08-11T19:36:51.415Z\",\"CreationDate\":\"2016-08-11T15:46:47.178Z\",\"InventoryRetrievalParameters\":{\"EndDate\":\"2016-08-11T23:46:50Z\",\"Format\":\"JSON\",\"Limit\":\"100\",\"Marker\":null,\"StartDate\":\"2016-08-07T07:12:32Z\"},\"InventorySizeInBytes\":124,\"JobDescription\":\"Biu inventory-retrievalin vault v01 at Thu Aug 11 23:46:50 CST 2016\",\"JobId\":\"eJg66cEaAFG7fOw5LC7utIUvX3RzN8O3Sjj4PKqNPyWSltjVCJno_eqszeMHs1FPcvNwnxgwMH3Un-JSgNKAcxTmTSko\",\"RetrievalByteRange\":null,\"SHA256TreeHash\":null,\"SNSTopic\":\"arn:aws-cn:sns:cn-north-1:296259413523:gp2-topic\",\"StatusCode\":\"Succeeded\",\"StatusMessage\":\"Succeeded\",\"VaultARN\":\"arn:aws-cn:glacier:cn-north-1:296259413523:vaults/v\"}\",\"Timestamp\" : \"2016-08-11T19:36:51.763Z\",\"SignatureVersion\" : \"1\",\"Signature\" : \"Ltql0cx2H3YCFxlzWJQL2p7mv3TVOCRBB5IeYjzyz4O0ukqGZKtmCB4gzhZ/2zpCEhjH2aapaVuWj4c8kfLNJ7DivJoUH1enqLpZ4inY9ex5Hw1Z6H8kT3jspPoJhDAnCxNpFOCyv57p4XjogWcS9KLdssrUbeEL+sv275YR40TXb6lAh1UjccSkGzdQbwt7/qPCMrjHFDr6731eZSCMlEcsM3OwrJ1d08HhmK+sIVMp+Fp3i8UHu0xJkEULzBvwquXQ5Jo0ToOQs8WqDqnSaQKrT2KxAVblH1zljCW5Ki83UbZHpIaifySPYCrO8K6P0HBC8BxJ47MkvnKLc8qtRQ==\",\"SigningCertURL\" : \"https://sns.cn-north-1.amazonaws.com.cn/SimpleNotificationService-53041e288642ce580860ab2866a6e8a9.pem\",\"UnsubscribeURL\" : \"https://sns.cn-north-1.amazonaws.com.cn/?Action=Unsubscribe&SubscriptionArn=arn:aws-cn:sns:cn-north-1:296259413523:gp2-topic:3ac84ee3-18ae-425f-bafa-26317c18569c\"}";
		SNSUtil util = new SNSUtil();
		GlacierUtil gutil = new GlacierUtil();
		SNSNotification notification = util.evaluateSNSNotification(j);
		SNSNotificationMessageGlacierJobRetrieveInventory message = gutil.evaluateSNSMessageGlacierJobRetrieveInventory(notification.getMessage());
		System.out.println(message.getInventoryRetrievalParameters());
		GlacierInventoryRetrievalParameters parameters = gutil.evaludateSNSGlacierInventoryRetrievalParameters(message.getInventoryRetrievalParameters());
		System.out.println(parameters.getFormat());
		
		/*
		String simpleMessage="{\"id\":10,\"name\":\"bgl\",\"person\":{\"address\":\"haha\",\"phone\":\"1231231\"}}";
		TestMessage tm = Jackson.fromJsonString(simpleMessage,TestMessage.class);
		System.out.println(tm.getName()+tm.getPerson().getPhone());
		/**/
	}
}
