package bglutil.common;

import com.amazonaws.services.config.AmazonConfig;
import com.amazonaws.services.config.model.ConfigurationRecorder;

public class ConfigUtil implements IUtil{
	public void printAllPhysicalId(Object config){
		for(ConfigurationRecorder cr:((AmazonConfig)config).describeConfigurationRecorders().getConfigurationRecorders()){
			System.out.println("config-recorder: "+cr.getName()+", role: "+cr.getRoleARN());
		}
	}
}
