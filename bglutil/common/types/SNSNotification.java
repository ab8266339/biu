package bglutil.common.types;

public class SNSNotification {
	public SNSNotification() {
		super();
	}
	public SNSNotification(String type, String messageId, String topicArn,
			String message, String timestamp, String signatureVersion,
			String signature, String signingCertURL, String unsubscribeURL) {
		super();
		this.type = type;
		this.messageId = messageId;
		this.topicArn = topicArn;
		this.message = message;
		this.timestamp = timestamp;
		this.signatureVersion = signatureVersion;
		this.signature = signature;
		this.signingCertURL = signingCertURL;
		this.unsubscribeURL = unsubscribeURL;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getTopicArn() {
		return topicArn;
	}
	public void setTopicArn(String topicArn) {
		this.topicArn = topicArn;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSignatureVersion() {
		return signatureVersion;
	}
	public void setSignatureVersion(String signatureVersion) {
		this.signatureVersion = signatureVersion;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getSigningCertURL() {
		return signingCertURL;
	}
	public void setSigningCertURL(String signingCertURL) {
		this.signingCertURL = signingCertURL;
	}
	public String getUnsubscribeURL() {
		return unsubscribeURL;
	}
	public void setUnsubscribeURL(String unsubscribeURL) {
		this.unsubscribeURL = unsubscribeURL;
	}
	private String type;
	private String messageId;
	private String topicArn;
	private String message;
	private String timestamp;
	private String signatureVersion;
	private String signature;
	private String signingCertURL;
	private String unsubscribeURL;
}
