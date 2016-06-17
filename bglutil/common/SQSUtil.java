package bglutil.common;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class SQSUtil{
	
	private String getQueueUrl(String accountId, Regions region, String name){
		String queueUrl = region.equals(Regions.CN_NORTH_1)?
				"https://sqs."+region.getName()+".amazonaws.com.cn/"+accountId+"/"+name:
				"https://sqs."+region.getName()+".amazonaws.com/"+accountId+"/"+name;
		return queueUrl;
	}
	
	public void printAllPhysicalId(AmazonSQS sqs){
		for(String url:sqs.listQueues().getQueueUrls()){
			System.out.println("sqs: "+url);
		}
	}
	
	public void showAllMessageInQueue(AmazonSQS sqs, String accountId, Regions region, String name, int peekCount){
		String queueUrl = this.getQueueUrl(accountId, region, name);
		for(int i=0;i<peekCount;i++){
			for(Message m:sqs.receiveMessage(new ReceiveMessageRequest().withQueueUrl(queueUrl).withVisibilityTimeout(2).withMaxNumberOfMessages(10)).getMessages()){
				System.out.println("=> Message Body Start \\");
				System.out.println(m.getBody().replaceAll("\\\\", ""));
				System.out.println("=> Message Body End /");
				
			}
		}
	}
	
	
	public void dropQueueByName(AmazonSQS sqs, String accountId, Regions region, String name){
		
		String queueUrl = this.getQueueUrl(accountId, region, name);
		System.out.println("Deleting "+queueUrl);
		DeleteQueueRequest request = new DeleteQueueRequest()
										.withQueueUrl(queueUrl);
		sqs.deleteQueue(request);
	}
}
