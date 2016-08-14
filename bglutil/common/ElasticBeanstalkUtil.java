package bglutil.common;

import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk;
import com.amazonaws.services.elasticbeanstalk.model.ApplicationDescription;

public class ElasticBeanstalkUtil implements IUtil{
	public void printAllPhysicalId(Object eb){
		for(ApplicationDescription ad: ((AWSElasticBeanstalk)eb).describeApplications().getApplications()){
			String app = ad.getApplicationName();
			StringBuffer sb = new StringBuffer();
			for(String version: ad.getVersions()){
				sb.append(", "+version);
			}
			System.out.println("elasticbeanstalk-app: "+app+new String(sb));
		}
	}
}
