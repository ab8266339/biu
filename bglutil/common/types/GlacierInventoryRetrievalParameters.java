package bglutil.common.types;

public class GlacierInventoryRetrievalParameters {
	private String endDate;
	private String format;
	private Long limit;
	private String marker;
	private String startDate;
	public GlacierInventoryRetrievalParameters() {
		super();
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Long getLimit() {
		return limit;
	}
	public void setLimit(Long limit) {
		this.limit = limit;
	}
	public String getMarker() {
		return marker;
	}
	public void setMarker(String marker) {
		this.marker = marker;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public GlacierInventoryRetrievalParameters(String endDate, String format,
			Long limit, String marker, String startDate) {
		super();
		this.endDate = endDate;
		this.format = format;
		this.limit = limit;
		this.marker = marker;
		this.startDate = startDate;
	}
}
