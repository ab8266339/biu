package bglutil.common;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.FunctionConfiguration;

public class LambdaUtil implements IUtil{
	public void printAllPhysicalId(Object lambda){
		for(FunctionConfiguration fc:((AWSLambda)lambda).listFunctions().getFunctions()){
			System.out.println("lambda: "+fc.getFunctionName());
		}
	}
}
