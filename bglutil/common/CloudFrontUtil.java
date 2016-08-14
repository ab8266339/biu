package bglutil.common;


import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudfront.model.DeleteDistributionRequest;
import com.amazonaws.services.cloudfront.model.DistributionSummary;
import com.amazonaws.services.cloudfront.model.ListDistributionsRequest;
import com.amazonaws.services.cloudfront.model.Origin;

public class CloudFrontUtil implements IUtil{
	public void printAllPhysicalId(Object cf){
		for(DistributionSummary ds:((AmazonCloudFrontClient)cf).listDistributions(new ListDistributionsRequest()).getDistributionList().getItems()){
			System.out.println("cloudfront: "+ds.getId()+", "+ds.getStatus()+", endabled("+ds.getEnabled()+")");
			for(Origin o:ds.getOrigins().getItems()){
				System.out.println("\torigin: "+o.getDomainName()+"/"+o.getOriginPath());
			}
		}
	}
	
	public void deleteDistribution(AmazonCloudFront cf, String id){
		cf.deleteDistribution(new DeleteDistributionRequest().withId(id));
		System.out.println("=> Deleting Distribution " + id + " requested");
	}
}
