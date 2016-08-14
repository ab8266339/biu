package bglutil.common;

import com.amazonaws.services.cloudhsm.AWSCloudHSM;

public class CloudHSMUtil implements IUtil {

	@Override
	public void printAllPhysicalId(Object o) {
		for(String hsmArn:((AWSCloudHSM)o).listHsms().getHsmList()){
			System.out.println("hsm: "+hsmArn);
		}
	}
}
