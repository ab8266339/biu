package bglutil.common.types;

public class S3EventRecord {
	//{"eventVersion":"2.0",
	//"eventSource":"aws:s3",
	//"awsRegion":"cn-north-1",
	//"eventTime":"2016-08-18T03:45:44.265Z",
	//"eventName":"ObjectCreated:Put",
	//"userIdentity":{"principalId":"AWS:AIDAPBXGUDOWTBI4C6GKM"},
	//"requestParameters":{"sourceIPAddress":"222.75.94.45"},
	//"responseElements":{"x-amz-request-id":"4FF196F8BBFAD9F2","x-amz-id-2":"Kzr8sXMeWhzowKY52zkkEWqpOEr00ziXEajGtrS0TjQFbWeY7DturhsBWO0HG9MotFmrW8be7rw="},
	//"s3":{"s3SchemaVersion":"1.0","configurationId":"devops-simple-app-commit-event",
		//"bucket":{"name":"glbao-beijing","ownerIdentity":{"principalId":"AWS:296259413523"},"arn":"arn:aws:s3:::glbao-beijing"},
		//"object":{"key":"devops-simple-app/document-root-china/document-root-china.tar.gz","size":532300,"eTag":"8ef175c48a7492e82f7fc6312d11c135","sequencer":"0057B52F64E5A4E427"}}}
	
	private String eventVersion;
	private String eventSource;
	private String awsRegion;
	private String eventTime;
	private String eventName;
	private String userIdentity;
	private String requestParameters;
	private String responseElements;
	private String s3;
	
	public S3EventRecord() {
		super();
	}
	public S3EventRecord(String eventVersion, String eventSource, String awsRegion, String eventTime, String eventName,
			String userIdentity, String requestParameters, String responseElements, String s3) {
		super();
		this.eventVersion = eventVersion;
		this.eventSource = eventSource;
		this.awsRegion = awsRegion;
		this.eventTime = eventTime;
		this.eventName = eventName;
		this.userIdentity = userIdentity;
		this.requestParameters = requestParameters;
		this.responseElements = responseElements;
		this.s3 = s3;
	}
	public String getEventVersion() {
		return eventVersion;
	}
	public void setEventVersion(String eventVersion) {
		this.eventVersion = eventVersion;
	}
	public String getEventSource() {
		return eventSource;
	}
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
	public String getAwsRegion() {
		return awsRegion;
	}
	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getUserIdentity() {
		return userIdentity;
	}
	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}
	public String getRequestParameters() {
		return requestParameters;
	}
	public void setRequestParameters(String requestParameters) {
		this.requestParameters = requestParameters;
	}
	public String getResponseElements() {
		return responseElements;
	}
	public void setResponseElements(String responseElements) {
		this.responseElements = responseElements;
	}
	public String getS3() {
		return s3;
	}
	public void setS3(String s3) {
		this.s3 = s3;
	}
	
}
