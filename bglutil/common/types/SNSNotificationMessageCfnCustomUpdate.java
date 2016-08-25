package bglutil.common.types;

public class SNSNotificationMessageCfnCustomUpdate {
	// "Message" : 
	// "{"RequestType":"Update",
	// "ServiceToken":"arn:aws-cn:sns:cn-north-1:296259413523:gp2-topic",
	// "ResponseURL":"https://cloudformation-custom-resource-response-cnnorth1.s3.cn-north-1.amazonaws.com.cn/arn%3Aaws-cn%3Acloudformation%3Acn-north-1%3A296259413523%3Astack/dropme/e6b7c400-6554-11e6-91a6-50fa18a0d262%7CCIServerTest%7C877f27d4-e20a-47a5-9f70-51269804da58?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20160818T151938Z&X-Amz-SignedHeaders=host&X-Amz-Expires=7200&X-Amz-Credential=AKIAOMTUJJXMXKXJZEPQ%2F20160818%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Signature=7445ded2a18179c486f80d01f5585f73f7099e12196209baa90914b221791f2e",
	// "StackId":"arn:aws-cn:cloudformation:cn-north-1:296259413523:stack/dropme/e6b7c400-6554-11e6-91a6-50fa18a0d262",
	// "RequestId":"877f27d4-e20a-47a5-9f70-51269804da58",
	// "LogicalResourceId":"CIServerTest",
	// "PhysicalResourceId":"bgl-managed-7dddd67c-8ae2-4a3f-af41-a8cbf7130cb3",
	// "ResourceType":"AWS::CloudFormation::CustomResource",
	// "ResourceProperties":{
		//"ServiceToken":"arn:aws-cn:sns:cn-north-1:296259413523:gp2-topic",
		// "TestDns1":"ec2-54-222-200-123.cn-north-1.compute.amazonaws.com.cn/index.html",
		// "StackName":"dropme"},
	// "OldResourceProperties":{
		//"ServiceToken":"arn:aws-cn:sns:cn-north-1:296259413523:gp2-topic",
		// "TestDns":"ec2-54-222-200-123.cn-north-1.compute.amazonaws.com.cn/view/All/builds",
		// "StackName":"dropme"}}"
	private String requestType;
	private String serviceToken;
	private String responseUrl;
	private String stackId;
	private String requestId;
	private String logicalResourceId;
	private String pyhsicalResourceId;
	private String resourceType;
	private String resourceProperties;
	private String oldResourceProperties;
	
	public SNSNotificationMessageCfnCustomUpdate() {
		super();
	}
	public SNSNotificationMessageCfnCustomUpdate(String requestType, String serviceToken, String responseUrl,
			String stackId, String requestId, String logicalResourceId, String pyhsicalResourceId, String resourceType,
			String resourceProperties, String oldResourceProperties) {
		super();
		this.requestType = requestType;
		this.serviceToken = serviceToken;
		this.responseUrl = responseUrl;
		this.stackId = stackId;
		this.requestId = requestId;
		this.logicalResourceId = logicalResourceId;
		this.pyhsicalResourceId = pyhsicalResourceId;
		this.resourceType = resourceType;
		this.resourceProperties = resourceProperties;
		this.oldResourceProperties = oldResourceProperties;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getServiceToken() {
		return serviceToken;
	}
	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
	}
	public String getResponseUrl() {
		return responseUrl;
	}
	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}
	public String getStackId() {
		return stackId;
	}
	public void setStackId(String stackId) {
		this.stackId = stackId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getLogicalResourceId() {
		return logicalResourceId;
	}
	public void setLogicalResourceId(String logicalResourceId) {
		this.logicalResourceId = logicalResourceId;
	}
	public String getPyhsicalResourceId() {
		return pyhsicalResourceId;
	}
	public void setPyhsicalResourceId(String pyhsicalResourceId) {
		this.pyhsicalResourceId = pyhsicalResourceId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceProperties() {
		return resourceProperties;
	}
	public void setResourceProperties(String resourceProperties) {
		this.resourceProperties = resourceProperties;
	}
	public String getOldResourceProperties() {
		return oldResourceProperties;
	}
	public void setOldResourceProperties(String oldResourceProperties) {
		this.oldResourceProperties = oldResourceProperties;
	}
	
}
