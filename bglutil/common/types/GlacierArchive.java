package bglutil.common.types;

public class GlacierArchive {
	private String archiveId;
	private String archiveDescription;
	private String creationDate;
	private Long size;
	public String getArchiveId() {
		return archiveId;
	}
	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}
	public String getArchiveDescription() {
		return archiveDescription;
	}
	public void setArchiveDescription(String archiveDescription) {
		this.archiveDescription = archiveDescription;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getSha256TreeHash() {
		return sha256TreeHash;
	}
	public void setSha256TreeHash(String sha256TreeHash) {
		this.sha256TreeHash = sha256TreeHash;
	}
	public GlacierArchive(String archiveId, String archiveDescription,
			String creationDate, Long size, String sha256TreeHash) {
		super();
		this.archiveId = archiveId;
		this.archiveDescription = archiveDescription;
		this.creationDate = creationDate;
		this.size = size;
		this.sha256TreeHash = sha256TreeHash;
	}
	public GlacierArchive() {
		super();
	}
	private String sha256TreeHash;
}
