package bglutil.common.types;

public class SQSMessageBodyS3Event {
	// {"Records":[{"eventVersion":"2.0","eventSource":"aws:s3","awsRegion":"cn-north-1","eventTime":"2016-08-18T03:45:44.265Z","eventName":"ObjectCreated:Put","userIdentity":{"principalId":"AWS:AIDAPBXGUDOWTBI4C6GKM"},"requestParameters":{"sourceIPAddress":"222.75.94.45"},"responseElements":{"x-amz-request-id":"4FF196F8BBFAD9F2","x-amz-id-2":"Kzr8sXMeWhzowKY52zkkEWqpOEr00ziXEajGtrS0TjQFbWeY7DturhsBWO0HG9MotFmrW8be7rw="},"s3":{"s3SchemaVersion":"1.0","configurationId":"devops-simple-app-commit-event","bucket":{"name":"glbao-beijing","ownerIdentity":{"principalId":"AWS:296259413523"},"arn":"arn:aws:s3:::glbao-beijing"},"object":{"key":"devops-simple-app/document-root-china/document-root-china.tar.gz","size":532300,"eTag":"8ef175c48a7492e82f7fc6312d11c135","sequencer":"0057B52F64E5A4E427"}}}]}
	private String records;
	public void setRecords(String records){
		this.records = records;
	}
	public String getRecords(){
		return this.records;
	}
	public SQSMessageBodyS3Event() {
		super();
	}
	public SQSMessageBodyS3Event(String records){
		this.records = records;
	}
}