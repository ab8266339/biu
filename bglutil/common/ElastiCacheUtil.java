package bglutil.common;

import com.amazonaws.services.elasticache.AmazonElastiCache;
import com.amazonaws.services.elasticache.model.CacheCluster;
import com.amazonaws.services.elasticache.model.DeleteCacheClusterRequest;

public class ElasticacheUtil implements IUtil{
	
	public void printAllPhysicalId(Object cache){
		for(CacheCluster cc:((AmazonElastiCache)cache).describeCacheClusters().getCacheClusters()){
			System.out.println("elasticache: "+cc.getCacheClusterId());
		}
	}
	
	public void dropCacheClusterById(AmazonElastiCache cache, String id){
		DeleteCacheClusterRequest request = new DeleteCacheClusterRequest()
											.withCacheClusterId(id);
		System.out.println("=> Dropping "+id);
		cache.deleteCacheCluster(request);
	}
}
