package bglutil.common.types;

public class S3EventRecordS3BucketOwnerIdentity {

	//"ownerIdentity":{"principalId":"AWS:296259413523"}

	private String principalId;

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public S3EventRecordS3BucketOwnerIdentity(String principalId) {
		super();
		this.principalId = principalId;
	}

	public S3EventRecordS3BucketOwnerIdentity() {
		super();
	}
	
	
}
