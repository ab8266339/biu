package bglutil.common;

import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.model.Cluster;

public class EC2ContainerServiceUtil implements IUtil{
	public void printAllPhysicalId(Object ecs){
		for(Cluster cluster:((AmazonECS)ecs).describeClusters().getClusters()){
			System.out.println("ecs-cluster: "+cluster.getClusterName());
		}
	}
}
