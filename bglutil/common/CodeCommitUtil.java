package bglutil.common;

import java.util.List;

import com.amazonaws.services.codecommit.AWSCodeCommit;
import com.amazonaws.services.codecommit.model.GetRepositoryRequest;
import com.amazonaws.services.codecommit.model.ListRepositoriesRequest;
import com.amazonaws.services.codecommit.model.RepositoryMetadata;
import com.amazonaws.services.codecommit.model.RepositoryNameIdPair;
import com.amazonaws.services.codecommit.model.SortByEnum;

public class CodeCommitUtil implements IUtil{
	public void printAllPhysicalId(Object o){
		AWSCodeCommit acc = (AWSCodeCommit)o;
		List<RepositoryNameIdPair> repos = acc.listRepositories(new ListRepositoriesRequest().withSortBy(SortByEnum.RepositoryName)).getRepositories();
		for(RepositoryNameIdPair r:repos){
			RepositoryMetadata meta = acc.getRepository(new GetRepositoryRequest().withRepositoryName(r.getRepositoryName())).getRepositoryMetadata();
			System.out.println(
					meta.getArn()+", "+
					meta.getCloneUrlSsh());
		}
	}
}
