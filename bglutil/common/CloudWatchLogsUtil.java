package bglutil.common;

import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.model.DescribeMetricFiltersRequest;
import com.amazonaws.services.logs.model.LogGroup;
import com.amazonaws.services.logs.model.MetricFilter;

public class CloudWatchLogsUtil implements IUtil{
	
	public void printAllPhysicalId(Object o){
		AWSLogs logs = (AWSLogs) o;
		for(LogGroup lg:logs.describeLogGroups().getLogGroups()){
			System.out.println("log-group: "+lg.getLogGroupName()+", "+lg.getArn()+", (filters):"+lg.getMetricFilterCount());
			for(MetricFilter mf:logs.describeMetricFilters(new DescribeMetricFiltersRequest().withLogGroupName(lg.getLogGroupName())).getMetricFilters()){
				System.out.println("\tmetric-filter: "+mf.getFilterName()+", "+mf.getFilterPattern());
			}
		}	
	}
	
}
