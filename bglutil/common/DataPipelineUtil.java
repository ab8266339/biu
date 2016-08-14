package bglutil.common;

import com.amazonaws.services.datapipeline.DataPipeline;
import com.amazonaws.services.datapipeline.model.PipelineIdName;

public class DataPipelineUtil implements IUtil{
	public void printAllPhysicalId(Object datapipeline){
		for(PipelineIdName name: ((DataPipeline)datapipeline).listPipelines().getPipelineIdList()){
			System.out.println("pipeline: "+name.getName()+", "+name.getId());
		}
	}
}