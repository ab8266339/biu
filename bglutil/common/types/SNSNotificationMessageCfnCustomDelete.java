package bglutil.common.types;

public class SNSNotificationMessageCfnCustomDelete {
	// "Message" : 
	// "{"RequestType":"Delete",
	// "ServiceToken":"arn:aws-cn:sns:cn-north-1:296259413523:gp2-topic",
	// "ResponseURL":"https://cloudformation-custom-resource-response-cnnorth1.s3.cn-north-1.amazonaws.com.cn/arn%3Aaws-cn%3Acloudformation%3Acn-north-1%3A296259413523%3Astack/dropme/467c5470-6549-11e6-aed7-50d5cdfd10fa%7CCIServerTest%7C91126ada-82e5-42a4-b45a-37f14cdff905?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20160818T140227Z&X-Amz-SignedHeaders=host&X-Amz-Expires=7199&X-Amz-Credential=AKIAOMTUJJXMXKXJZEPQ%2F20160818%2Fcn-north-1%2Fs3%2Faws4_request&X-Amz-Signature=1f03f8ea09f180e4b785bb384ce30392ad068e260eea7846e451d32e7119e608",
	// "StackId":"arn:aws-cn:cloudformation:cn-north-1:296259413523:stack/dropme/467c5470-6549-11e6-aed7-50d5cdfd10fa",
	// "RequestId":"91126ada-82e5-42a4-b45a-37f14cdff905",
	// "LogicalResourceId":"CIServerTest",
	// "PhysicalResourceId":"dropme-CIServerTest-1CSUXRDAWQ7WW",
	// "ResourceType":"AWS::CloudFormation::CustomResource",
	// "ResourceProperties":
		//{"ServiceToken":"arn:aws-cn:sns:cn-north-1:296259413523:gp2-topic",
		//"TestDns":"ec2-54-222-200-123.cn-north-1.compute.amazonaws.com.cn/view/All/builds",
		//"StackName":"dropme"}}", //TODO
	
	private String requestType;
	private String serviceToken;
	private String responseUrl;
	private String stackId;
	private String requestId;
	private String logicalResourceId;
	private String pyhsicalResourceId;
	private String resourceType;
	private String resourceProperties;
	
	public SNSNotificationMessageCfnCustomDelete() {
		super();
	}
	public SNSNotificationMessageCfnCustomDelete(String requestType, String serviceToken, String responseUrl,
			String stackId, String requestId, String logicalResourceId, String pyhsicalResourceId, String resourceType,
			String resourceProperties) {
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
	
}
