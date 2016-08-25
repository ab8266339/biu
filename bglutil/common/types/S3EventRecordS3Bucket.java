package bglutil.common.types;

public class S3EventRecordS3Bucket {
	
	//"bucket":{
	//"name":"glbao-beijing",
	//"ownerIdentity":{"principalId":"AWS:296259413523"},
	//"arn":"arn:aws:s3:::glbao-beijing"},

	private String name;
	private String ownerIdentity;
	private String arn;
	
	public S3EventRecordS3Bucket() {
		super();
	}
	public S3EventRecordS3Bucket(String name, String ownerIdentity, String arn) {
		super();
		this.name = name;
		this.ownerIdentity = ownerIdentity;
		this.arn = arn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwnerIdentity() {
		return ownerIdentity;
	}
	public void setOwnerIdentity(String ownerIdentity) {
		this.ownerIdentity = ownerIdentity;
	}
	public String getArn() {
		return arn;
	}
	public void setArn(String arn) {
		this.arn = arn;
	}
	
}
