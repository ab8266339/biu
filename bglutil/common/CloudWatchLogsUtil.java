package bglutil.common;

import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.model.LogGroup;

public class CloudWatchLogsUtil implements IUtil{
	
	public void printAllPhysicalId(Object logs){
		for(LogGroup lg:((AWSLogs)logs).describeLogGroups().getLogGroups()){
			System.out.println("log-group: "+lg.getLogGroupName()+", "+lg.getArn());
		}
	}
}
