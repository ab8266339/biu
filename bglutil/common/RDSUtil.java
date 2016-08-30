package bglutil.common;

import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DBSnapshot;
import com.amazonaws.services.rds.model.DeleteDBInstanceRequest;
import com.amazonaws.services.rds.model.DeleteDBSnapshotRequest;

public class RDSUtil implements IUtil{
	public void printAllPhysicalId(Object o){
		AmazonRDS rds = (AmazonRDS)o;
		for(DBInstance instance:rds.describeDBInstances().getDBInstances()){
			System.out.println("rds: "+instance.getDBInstanceIdentifier()+", "+instance.getEndpoint().getAddress()+":"+instance.getEndpoint().getPort()+"/"+instance.getDBName());
		}
		for(DBSnapshot snapshot:rds.describeDBSnapshots().getDBSnapshots()){
			System.out.println("rds-snapshot: "+snapshot.getDBSnapshotIdentifier()+", "+snapshot.getSnapshotType()+", "+snapshot.getEngine()+", "+snapshot.getSnapshotCreateTime().toString());
		}
	}
	
	public void deleteDBSnapshot(AmazonRDS rds, String snapshotId){
		rds.deleteDBSnapshot(new DeleteDBSnapshotRequest().withDBSnapshotIdentifier(snapshotId));
		System.out.println("=> Deleting DBSnapshot "+snapshotId);
	}
	
	public void deleteDBInstance(AmazonRDS rds, String dbid){
		rds.deleteDBInstance(new DeleteDBInstanceRequest().withDBInstanceIdentifier(dbid).withSkipFinalSnapshot(true));
		System.out.println("=> Deleting RDS instance "+dbid);
	}
	
}
