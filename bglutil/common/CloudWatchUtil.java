package bglutil.common;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.MetricAlarm;

public class CloudWatchUtil implements IUtil{
	public void printAllPhysicalId(Object cw){
		for(MetricAlarm ma:((AmazonCloudWatch)cw).describeAlarms().getMetricAlarms()){
			System.out.println("monitoring-alarm: "+ma.getAlarmArn());
		}
	}
}
