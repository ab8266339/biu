package bglutil.common;

import com.amazonaws.services.cloudtrail.AWSCloudTrail;
import com.amazonaws.services.cloudtrail.model.Trail;

public class CloudTrailUtil implements IUtil{

	@Override
	public void printAllPhysicalId(Object o) {
		AWSCloudTrail trail = (AWSCloudTrail)o;
		for(Trail t:trail.describeTrails().getTrailList()){
			System.out.println("trail: "+t.getTrailARN()+", "+t.getS3BucketName()+"/"+t.getS3KeyPrefix()+", isGlobal:"+t.isIncludeGlobalServiceEvents());
		}
	}

}
