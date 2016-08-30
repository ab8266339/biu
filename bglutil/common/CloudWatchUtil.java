package bglutil.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricAlarm;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

public class CloudWatchUtil implements IUtil{
	public void printAllPhysicalId(Object cw){
		for(MetricAlarm ma:((AmazonCloudWatch)cw).describeAlarms().getMetricAlarms()){
			System.out.println("monitoring-alarm: "+ma.getAlarmArn());
		}
	}
	
	public void cloudWatchDemoBeijingAQI(AmazonCloudWatch cw, int waitSec, int loopCount){
		Random aqiRandom = new Random();
		MetricDatum md = new MetricDatum()
			.withMetricName("PM2.5 Monitor")
			.withDimensions(new Dimension().withName("AQI").withValue("Beijing"))
			.withUnit(StandardUnit.None);
		PutMetricDataRequest pdr = new PutMetricDataRequest()
								.withNamespace("Biu/Demo");
		ArrayList<MetricDatum> metricCollection = new ArrayList<MetricDatum>();
		Double aqiCollected = null;
		for(int i=0;i<loopCount;i++){
			aqiCollected = new Double(aqiRandom.nextInt(800));
			md.setTimestamp(new Date());
			md.setValue(aqiCollected);
			metricCollection.clear();
			metricCollection.add(md);
			pdr.setMetricData(metricCollection);
			cw.putMetricData(pdr);
			System.out.println("AQI: "+aqiCollected+", ("+i+")");
			try {
				Thread.sleep(1000*waitSec);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
