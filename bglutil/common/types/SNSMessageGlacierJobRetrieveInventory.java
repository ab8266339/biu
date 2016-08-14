package bglutil.common.types;

public class SNSMessageGlacierJobRetrieveInventory {
	private String action;
	private String archiveId;
	private String archiveSha256TreeHash;
	private Long archiveSizeInBytes;
	private String completed;
	private String completionDate;
	private String creationDate;
	private String inventoryRetrievalParameters;
	private Long inventorySizeInBytes;
	private String jobDescription;
	private String jobId;
	private Long retrievalByteRange;
	private String sha256TreeHash;
	private String snsTopic;
	private String statusCode;
	private String statusMessage;
	private String vaultArn;
	public SNSMessageGlacierJobRetrieveInventory(String action,
			String archiveId, String archiveSHA256TreeHash,
			Long archiveSizeInBytes, String completed, String completionDate,
			String creationDate,
			String inventoryRetrievalParameters,
			Long inventorySizeInBytes, String jobDescription, String jobId,
			Long retrievalByteRange, String sha256TreeHash, String snsTopic,
			String statusCode, String statusMessage, String vaultArn) {
		super();
		this.action = action;
		this.archiveId = archiveId;
		this.archiveSha256TreeHash = archiveSHA256TreeHash;
		this.archiveSizeInBytes = archiveSizeInBytes;
		this.completed = completed;
		this.completionDate = completionDate;
		this.creationDate = creationDate;
		this.inventoryRetrievalParameters = inventoryRetrievalParameters;
		this.inventorySizeInBytes = inventorySizeInBytes;
		this.jobDescription = jobDescription;
		this.jobId = jobId;
		this.retrievalByteRange = retrievalByteRange;
		this.sha256TreeHash = sha256TreeHash;
		this.snsTopic = snsTopic;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.vaultArn = vaultArn;
	}
	public SNSMessageGlacierJobRetrieveInventory() {
		super();
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getArchiveId() {
		return archiveId;
	}
	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}
	public String getArchiveSha256TreeHash() {
		return archiveSha256TreeHash;
	}
	public void setArchiveSha256TreeHash(String archiveSha256TreeHash) {
		this.archiveSha256TreeHash = archiveSha256TreeHash;
	}
	public Long getArchiveSizeInBytes() {
		return archiveSizeInBytes;
	}
	public void setArchiveSizeInBytes(Long archiveSizeInBytes) {
		this.archiveSizeInBytes = archiveSizeInBytes;
	}
	public String getCompleted() {
		return completed;
	}
	public void setCompleted(String completed) {
		this.completed = completed;
	}
	public String getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getInventoryRetrievalParameters() {
		return this.inventoryRetrievalParameters;
	}
	public void setInventoryRetrievalParameters(
			String inventoryRetrievalParameters) {
		this.inventoryRetrievalParameters = inventoryRetrievalParameters;
	}
	public Long getInventorySizeInBytes() {
		return inventorySizeInBytes;
	}
	public void setInventorySizeInBytes(Long inventorySizeInBytes) {
		this.inventorySizeInBytes = inventorySizeInBytes;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public Long getRetrievalByteRange() {
		return retrievalByteRange;
	}
	public void setRetrievalByteRange(Long retrievalByteRange) {
		this.retrievalByteRange = retrievalByteRange;
	}
	public String getSha256TreeHash() {
		return sha256TreeHash;
	}
	public void setSha256TreeHash(String sha256TreeHash) {
		this.sha256TreeHash = sha256TreeHash;
	}
	public String getSnsTopic() {
		return snsTopic;
	}
	public void setSnsTopic(String snsTopic) {
		this.snsTopic = snsTopic;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getVaultArn() {
		return vaultArn;
	}
	public void setVaultArn(String vaultArn) {
		this.vaultArn = vaultArn;
	}
	
}
