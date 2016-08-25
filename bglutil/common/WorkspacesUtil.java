package bglutil.common;

import com.amazonaws.services.directory.AWSDirectoryService;
import com.amazonaws.services.directory.model.DescribeDirectoriesRequest;
import com.amazonaws.services.workspaces.AmazonWorkspaces;
import com.amazonaws.services.workspaces.model.Workspace;

import bglutil.common.types.DirectoryServiceWorkspacesClients;

public class WorkspacesUtil implements IUtil{
	public void printAllPhysicalId(Object dsws){
		
		AmazonWorkspaces ws = ((DirectoryServiceWorkspacesClients)dsws).getWs();
		AWSDirectoryService ds = ((DirectoryServiceWorkspacesClients)dsws).getDs();
		for(Workspace workspace:ws.describeWorkspaces().getWorkspaces()){
			System.out.println("workspaces: "+workspace.getWorkspaceId()+", IP: "+workspace.getIpAddress()+", user: "+workspace.getUserName());
			String did = workspace.getDirectoryId();
			if(did!=null){
				String dName = ds.describeDirectories(new DescribeDirectoriesRequest().withDirectoryIds(did)).getDirectoryDescriptions().get(0).getName();
				System.out.println("\t Registered to directory: "+did+", "+dName);
			}
		}
		
	}
	
	
}
