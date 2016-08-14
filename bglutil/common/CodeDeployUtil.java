package bglutil.common;

import com.amazonaws.services.codedeploy.AmazonCodeDeploy;

public class CodeDeployUtil implements IUtil{
	public void printAllPhysicalId(Object codedeploy){
		for(String app:((AmazonCodeDeploy)codedeploy).listApplications().getApplications()){
			System.out.println("codedeploy-app: "+app);
		}
	}
}
