package bglutil.common;

import com.amazonaws.services.workspaces.AmazonWorkspaces;
import com.amazonaws.services.workspaces.model.Workspace;

public class WorkspacesUtil implements IUtil{
	public void printAllPhysicalId(Object workspaces){
		for(Workspace workspace:((AmazonWorkspaces)workspaces).describeWorkspaces().getWorkspaces()){
			System.out.println("workspaces: "+workspace.getWorkspaceId()+", IP: "+workspace.getIpAddress()+", user: "+workspace.getUserName());
		}
	}
}
