package bglutil.common;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class SQSUtil implements IUtil{
	
	private Helper h = new Helper();
	
	public String getQueueUrl(String queueName, String profile) throws Exception{
		String accountId = new STSUtil().getAccountId((AWSSecurityTokenService) Clients.getClientByServiceClassProfile(Clients.STS, profile));
		Regions currentRegion = Clients.getRegions(profile);
		String topicArn = Clients.isChinaRegion(currentRegion)?
				"https://sqs."+currentRegion+".amazonaws.com.cn/"+accountId+"/"+queueName:
				"https://sqs."+currentRegion+".amazonaws.com/"+accountId+"/"+queueName;
		return topicArn;
	}
	
	public void printAllPhysicalId(Object sqs){
		for(String url:((AmazonSQS)sqs).listQueues().getQueueUrls()){
			System.out.println("sqs: "+url);
		}
	}
	
	public void showAllMessageInQueue(AmazonSQS sqs, String queueName, int peekCount, boolean remove, String profile) throws Exception{
		String queueUrl = this.getQueueUrl(queueName, profile);
		for(int i=0;i<peekCount;i++){
			for(Message m:sqs.receiveMessage(new ReceiveMessageRequest().withQueueUrl(queueUrl).withVisibilityTimeout(2).withMaxNumberOfMessages(10)).getMessages()){
				h.title("=> Message Body Start");
				System.out.println(m.getBody().replaceAll("\\\\", ""));
				if(remove){
					sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(this.getQueueUrl(queueName, profile))
						.withReceiptHandle(m.getReceiptHandle()));
				}
				h.title("=> Message Body End");
			}
		}
	}
	
	public void dropQueueByName(String queueName, String profile) throws Exception{
		AmazonSQS sqs = (AmazonSQS) Clients.getClientByServiceClassProfile(Clients.SQS, profile);
		String queueUrl = this.getQueueUrl(queueName, profile);
		System.out.println("Deleting "+queueUrl);
		DeleteQueueRequest request = new DeleteQueueRequest()
										.withQueueUrl(queueUrl);
		sqs.deleteQueue(request);
	}
}
