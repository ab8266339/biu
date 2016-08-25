package bglutil.common.types;

import com.amazonaws.services.directory.AWSDirectoryService;
import com.amazonaws.services.workspaces.AmazonWorkspaces;

public class DirectoryServiceWorkspacesClients {
	private AWSDirectoryService ds;
	private AmazonWorkspaces ws;
	public AWSDirectoryService getDs() {
		return ds;
	}
	public void setDs(AWSDirectoryService ds) {
		this.ds = ds;
	}
	public AmazonWorkspaces getWs() {
		return ws;
	}
	public void setWs(AmazonWorkspaces ws) {
		this.ws = ws;
	}
	public DirectoryServiceWorkspacesClients(AWSDirectoryService ds, AmazonWorkspaces ws) {
		super();
		this.ds = ds;
		this.ws = ws;
	}
	
}
