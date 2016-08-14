package bglutil.common.types;

public class GlacierInventoryRetrievalJobOutput {
	private String vaultArn;
	private String inventoryDate;
	private String archiveList;
	public String getVaultArn() {
		return vaultArn;
	}
	public void setVaultArn(String vaultArn) {
		this.vaultArn = vaultArn;
	}
	public String getInventoryDate() {
		return inventoryDate;
	}
	public void setInventoryDate(String inventoryDate) {
		this.inventoryDate = inventoryDate;
	}
	public String getArchiveList() {
		return archiveList;
	}
	public void setArchiveList(String archiveList) {
		this.archiveList = archiveList;
	}
	public GlacierInventoryRetrievalJobOutput(String vaultArn,
			String inventoryDate, String archiveList) {
		super();
		this.vaultArn = vaultArn;
		this.inventoryDate = inventoryDate;
		this.archiveList = archiveList;
	}
	public GlacierInventoryRetrievalJobOutput() {
		super();
	}
}
