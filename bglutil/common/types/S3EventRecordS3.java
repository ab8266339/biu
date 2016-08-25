package bglutil.common.types;

public class S3EventRecordS3 {
	//"s3":{
	//"s3SchemaVersion":"1.0",
	//"configurationId":"devops-simple-app-commit-event",
	//"bucket":{"name":"glbao-beijing","ownerIdentity":{"principalId":"AWS:296259413523"},"arn":"arn:aws:s3:::glbao-beijing"}, //TODO
	//"object":{"key":"devops-simple-app/document-root-china/document-root-china.tar.gz","size":532300,"eTag":"8ef175c48a7492e82f7fc6312d11c135","sequencer":"0057B52F64E5A4E427"}}} //TODO
	
	private String s3SchemaVersion;
	private String configurationId;
	private String bucket;
	private String object;
	
	public S3EventRecordS3() {
		super();
	}
	public S3EventRecordS3(String s3SchemaVersion, String configurationId, String bucket, String object) {
		super();
		this.s3SchemaVersion = s3SchemaVersion;
		this.configurationId = configurationId;
		this.bucket = bucket;
		this.object = object;
	}
	public String getS3SchemaVersion() {
		return s3SchemaVersion;
	}
	public void setS3SchemaVersion(String s3SchemaVersion) {
		this.s3SchemaVersion = s3SchemaVersion;
	}
	public String getConfigurationId() {
		return configurationId;
	}
	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
}
