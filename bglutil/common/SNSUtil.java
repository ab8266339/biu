package bglutil.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bglutil.common.types.GlacierArchive;
import bglutil.common.types.GlacierInventoryRetrievalJobOutput;
import bglutil.common.types.SNSGlacierInventoryRetrievalParameters;
import bglutil.common.types.SNSMessageGlacierJobRetrieveInventory;
import bglutil.common.types.SNSNotification;
import bglutil.main.Biu;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

public class SNSUtil implements IUtil{
	
	public void printAllPhysicalId(Object sns){
		for(Topic t: ((AmazonSNS)sns).listTopics().getTopics()){
			System.out.println("sns: "+t.getTopicArn());
		}
	}
	
	public void publish(AmazonSNS sns, String topicArn, String subject, String message){
		sns.publish(new PublishRequest().withSubject(subject).withTopicArn(topicArn).withMessage(message));
	}
	
	public void publish(String topicName, String subject, String message, String profile) throws Exception{
		AmazonSNS sns = (AmazonSNS) Clients.getClientByServiceClassProfile(Clients.SNS, profile);
		String topicArn = this.getTopicArn(topicName, profile);
		this.publish(sns, topicArn, subject, message);
	}
	
	public String getTopicArn(String topicName, String profile) throws Exception{
		String accountId = new STSUtil().getAccountId((AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, profile));
		Regions currentRegion = Clients.getRegions(profile);
		String topicArn = Clients.isChinaRegion(currentRegion)?
				"arn:aws-cn:sns:"+currentRegion.getName()+":"+accountId+":"+topicName:
				"arn:aws:sns:"+currentRegion.getName()+":"+accountId+":"+topicName;
		return topicArn;
	}
	
	public void dropTopicByName(String topicName, String profile) throws Exception{
		AmazonSNS sns = (AmazonSNS) Clients.getClientByServiceClassProfile(Clients.SNS, profile);
		String topicArn = this.getTopicArn(topicName, profile);
		System.out.println("Deleting "+topicArn);
		DeleteTopicRequest request = new DeleteTopicRequest()
										.withTopicArn(topicArn);
		sns.deleteTopic(request);
	}
	
	// Glacier
	public SNSGlacierInventoryRetrievalParameters evaludateSNSGlacierInventoryRetrievalParameters(String jsonString){
		JsonNode n = GeneralUtil.getJsonNode(jsonString);
		return new SNSGlacierInventoryRetrievalParameters(
					n.get("EndDate").asText(),
					n.get("Format").asText(),
					n.get("Limit").asLong(),
					n.get("Marker").asText(),
					n.get("StartDate").asText()
				);
	}
	
	public SNSMessageGlacierJobRetrieveInventory evaluateSNSMessageGlacierJobRetrieveInventory(String jsonString){
		JsonNode n = GeneralUtil.getJsonNode(jsonString);
		return new SNSMessageGlacierJobRetrieveInventory(
					n.get("Action").asText(),
					n.get("ArchiveId").asText(),
					n.get("ArchiveSHA256TreeHash").asText(),
					n.get("ArchiveSizeInBytes").asLong(),
					n.get("Completed").asText(),
					n.get("CompletionDate").asText(),
					n.get("CreationDate").asText(),
					n.get("InventoryRetrievalParameters").toString(),
					n.get("InventorySizeInBytes").asLong(),
					n.get("JobDescription").asText(),
					n.get("JobId").asText(),
					n.get("RetrievalByteRange").asLong(),
					n.get("SHA256TreeHash").asText(),
					n.get("SNSTopic").asText(),
					n.get("StatusCode").asText(),
					n.get("StatusMessage").asText(),
					n.get("VaultARN").asText()
				);
	}
	
	public SNSNotification evaluateSNSNotification(String jsonString){
		JsonNode n = GeneralUtil.getJsonNode(jsonString);
		return new SNSNotification(
					n.get("Type").asText(),
					n.get("MessageId").asText(),
					n.get("TopicArn").asText(),
					n.get("Message").toString(),
					n.get("Timestamp").asText(),
					n.get("SignatureVersion").asText(),
					n.get("Signature").asText(),
					n.get("SigningCertURL").asText(),
					n.get("UnsubscribeURL").asText()
				);
	}
	
}
