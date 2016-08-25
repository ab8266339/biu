package bglutil.common;

import bglutil.common.types.SNSNotification;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.databind.JsonNode;

public class SNSUtil implements IUtil{
	
	public void printAllPhysicalId(Object o){
		AmazonSNS sns = (AmazonSNS) o;
		for(Topic t: ((AmazonSNS)sns).listTopics().getTopics()){
			System.out.println("sns: "+t.getTopicArn());
			for(Subscription s:sns.listSubscriptionsByTopic(t.getTopicArn()).getSubscriptions()){
				System.out.println("\t=> "+s.getProtocol()+", "+s.getEndpoint());
			}
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
	
	public SNSNotification evaluateSNSNotification(String jsonString){
		JsonNode n = GeneralUtil.getJsonNode(jsonString);
		return new SNSNotification(
					n.get("Type").asText(),
					n.get("MessageId").asText(),
					n.get("TopicArn").asText(),
					n.get("Subject").asText(),
					n.get("Message").toString(),
					n.get("Timestamp").asText(),
					n.get("SignatureVersion").asText(),
					n.get("Signature").asText(),
					n.get("SigningCertURL").asText(),
					n.get("UnsubscribeURL").asText());
	}
	
}
