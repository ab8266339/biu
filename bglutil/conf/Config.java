package bglutil.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	public static final String HOME = System.getProperty("user.home");
	
	public static final String BACKUP_BUCKET = "BACKUP_BUCKET";
	public static final String APP_NACL_BEIJING = "APP_NACL_BEIJING";
	public static final String DEFAULT_NACL_BEIJING = "DEFAULT_NACL_BEIJING";
	public static final String APP_NACL_VIR = "APP_NACL_VIR";
	public static final String DEFAULT_NACL_VIR = "DEFAULT_NACL_VIR";
	public static final String APP_NACL_TOKYO = "APP_NACL_TOKYO";
	public static final String DEFAULT_NACL_TOKYO = "DEFAULT_NACL_TOKYO";
	public static final String MFA_USERNAME = "MFA_USERNAME";
	public static final String MFA_ARN = "MFA_ARN";
	public static final String EXAMPLE_IAM_ROLE_ARN = "EXAMPLE_IAM_ROLE_ARN";
	public static final String BEIJING_EC2_KEYPAIR_NAME = "BEIJING_EC2_KEYPAIR_NAME";
	public static final String BEIJING_DEFAULT_VPC_SUBNET1 = "BEIJING_DEFAULT_VPC_SUBNET1";
	public static final String BEIJING_DEFAULT_VPC_SUBNET2 = "BEIJING_DEFAULT_VPC_SUBNET2";
	public static final String BEIJING_DEFAULT_VPC_ALLOWALL_SECURITY_GROUP = "BEIJING_DEFAULT_VPC_ALLOWALL_SECURITY_GROUP";
	public static final String BEIJING_DEMO_SQS_QUEUE_URL = "BEIJING_DEMO_SQS_QUEUE_URL";
	
	public String c(String name){
		Properties prop = new Properties();
        InputStream in = null;
        String setting = null;
		try {
			in = new FileInputStream(HOME + "/.aws/biu.properties");
			prop.load(in);
			in.close();
			setting = prop.getProperty(name);
		} catch (FileNotFoundException e) {
			System.out.println("Configuration file ~/.aws/biu.properties not exists.");
			System.exit(1);
		}
        catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return setting;
	}
}
