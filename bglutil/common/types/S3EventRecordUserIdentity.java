package bglutil.common.types;

public class S3EventRecordUserIdentity {
	// "userIdentity":{"principalId":"AWS:AIDAPBXGUDOWTBI4C6GKM"}
	private String pricipalId;

	public S3EventRecordUserIdentity() {
		super();
	}

	public S3EventRecordUserIdentity(String pricipalId) {
		super();
		this.pricipalId = pricipalId;
	}

	public String getPricipalId() {
		return pricipalId;
	}

	public void setPricipalId(String pricipalId) {
		this.pricipalId = pricipalId;
	}
}
