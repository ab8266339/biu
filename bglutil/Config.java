package bglutil;

public class Config {
	
	public static final String BACKUP_BUCKET = ""; // default bucket name.
	public static final String APP_NACL_BEIJING = ""; // custom NACL id in Beijing. 
	public static final String DEFAULT_NACL_BEIJING = ""; // default VPC NACL id in Beijing.
	public static final String APP_NACL_VIR = ""; // custom NACL id in Virginia.
	public static final String DEFAULT_NACL_VIR = ""; // default VPC NACL id in Virginia.
	public static final String APP_NACL_TOKYO = ""; // custom NACL id in Tokyo.
	public static final String DEFAULT_NACL_TOKYO = ""; // default VPC NACL id in Tokyo.
	public static final String MFA_USERNAME = ""; // IAM user name for MFA demo.
	public static final String MFA_ARN = ""; // IAM user MFA ARN for MFA demo.
	public static final String EXAMPLE_IAM_ROLE_ARN = ""; // cross account role ARN for assume role demo.
	public static final String BEIJING_EC2_KEYPAIR_NAME = ""; // EC2 key pair in Beijing.
	public static final String BEIJING_DEFAULT_VPC_SUBNET1 = ""; // custom VPC subnet 1 id.
	public static final String BEIJING_DEFAULT_VPC_SUBNET2 = ""; // custom VPC subnet 2 id, backup of subnet 1.
	public static final String BEIJING_DEFAULT_VPC_ALLOWALL_SECURITY_GROUP = ""; // custom VPC allow all security group id.
	
}
