package bglutil.common.types;

public class S3EventRecordS3Object {
	
	//"object":{
	//"key":"devops-simple-app/document-root-china/document-root-china.tar.gz",
	//"size":532300,
	//"eTag":"8ef175c48a7492e82f7fc6312d11c135",
	//"sequencer":"0057B52F64E5A4E427"}}}
	
	private String key;
	private Long size;
	private String eTag;
	private String sequencer;
	
	public S3EventRecordS3Object() {
		super();
	}
	public S3EventRecordS3Object(String key, Long size, String eTag, String sequencer) {
		super();
		this.key = key;
		this.size = size;
		this.eTag = eTag;
		this.sequencer = sequencer;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String geteTag() {
		return eTag;
	}
	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	public String getSequencer() {
		return sequencer;
	}
	public void setSequencer(String sequencer) {
		this.sequencer = sequencer;
	}
	
}
