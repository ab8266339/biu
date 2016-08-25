package bglutil.common.types;

public class S3EventRecordResponseElements {
	//"responseElements":{
	//"x-amz-request-id":"4FF196F8BBFAD9F2",
	//"x-amz-id-2":"Kzr8sXMeWhzowKY52zkkEWqpOEr00ziXEajGtrS0TjQFbWeY7DturhsBWO0HG9MotFmrW8be7rw="}, 
	
	private String xAmzRequestId;
	private String xAmzId2;
	public String getxAmzRequestId() {
		return xAmzRequestId;
	}
	public void setxAmzRequestId(String xAmzRequestId) {
		this.xAmzRequestId = xAmzRequestId;
	}
	public String getxAmzId2() {
		return xAmzId2;
	}
	public void setxAmzId2(String xAmzId2) {
		this.xAmzId2 = xAmzId2;
	}
	public S3EventRecordResponseElements(String xAmzRequestId, String xAmzId2) {
		super();
		this.xAmzRequestId = xAmzRequestId;
		this.xAmzId2 = xAmzId2;
	}
	public S3EventRecordResponseElements() {
		super();
	}
	
}
