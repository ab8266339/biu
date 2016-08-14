package bglutil.common;

import java.util.Map;

import com.amazonaws.services.cloudsearchv2.AmazonCloudSearch;

public class CloudSearchUtil implements IUtil{

	@Override
	public void printAllPhysicalId(Object o) {
		AmazonCloudSearch cs = (AmazonCloudSearch)o;
		Map<String,String> domainNames = cs.listDomainNames().getDomainNames();
		for(String key:domainNames.keySet()){
			System.out.println("key: "+key+", value:"+domainNames.get(key));
		}
	}

}
