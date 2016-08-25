package bglutil.common;

import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.model.DeleteStackRequest;
import com.amazonaws.services.cloudformation.model.Stack;

public class CloudFormationUtil implements IUtil{

	
	public void printAllPhysicalId(Object cfn){
		for(Stack stack:((AmazonCloudFormation)cfn).describeStacks().getStacks()){
			System.out.println("cloudformation-stack: "+stack.getStackName()+", "+stack.getStackId()+", "+stack.getStackStatus());
		}
	}
	
	public void deleteCNFStack(AmazonCloudFormation cfn, String stackName){
		DeleteStackRequest request = new DeleteStackRequest()
									.withStackName(stackName);
		System.out.println("=> Dropping CFN stack: "+stackName);
		cfn.deleteStack(request);
	}
}
