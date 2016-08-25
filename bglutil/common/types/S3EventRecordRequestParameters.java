package bglutil.common.types;

public class S3EventRecordRequestParameters {
	private String sourceIPAddress;
	public S3EventRecordRequestParameters(){
		super();
	}
	public S3EventRecordRequestParameters(String sourceIPAddress) {
		super();
		this.sourceIPAddress = sourceIPAddress;
	}
	public String getSourceIPAddress() {
		return sourceIPAddress;
	}
	public void setSourceIPAddress(String sourceIPAddress) {
		this.sourceIPAddress = sourceIPAddress;
	}
	
}
