package bglutil.common;

import com.amazonaws.services.directory.AWSDirectoryService;
import com.amazonaws.services.directory.model.DeleteDirectoryRequest;
import com.amazonaws.services.directory.model.DescribeDirectoriesRequest;
import com.amazonaws.services.directory.model.DirectoryDescription;
import com.amazonaws.services.directory.model.DisableSsoRequest;

public class DirectoryUtil implements IUtil{
	public void printAllPhysicalId(Object directory){
		
		for(DirectoryDescription dd:((AWSDirectoryService)directory).describeDirectories().getDirectoryDescriptions()){
			System.out.println("directory-id: "+dd.getDirectoryId()+", directory-name: "+dd.getName()+", "+dd.getAccessUrl()+", SSO: "+dd.getSsoEnabled());
		}
	}
	//TODO: com.amazonaws.services.directory.model.ClientException: Cannot delete the directory because it still has authorized applications.
	public void deleteDirectory(AWSDirectoryService ds, String directoryId){
		DirectoryDescription dd = ds.describeDirectories(new DescribeDirectoriesRequest().withDirectoryIds(directoryId)).getDirectoryDescriptions().get(0);
		if(dd.isSsoEnabled()){
			ds.disableSso(new DisableSsoRequest().withDirectoryId(directoryId));
			do{
				dd = ds.describeDirectories(new DescribeDirectoriesRequest().withDirectoryIds(directoryId)).getDirectoryDescriptions().get(0);
				Helper.wait(1000);
			}while(dd.isSsoEnabled());
		}
		String name = dd.getName();
		ds.deleteDirectory(new DeleteDirectoryRequest().withDirectoryId(directoryId));
		System.out.println("Deleting directory "+directoryId+", "+name);
	}
	
	public void createUser(AWSDirectoryService directory, String username){
		
	}
}
