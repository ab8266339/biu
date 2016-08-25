package bglutil.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import bglutil.common.types.GlacierArchive;
import bglutil.common.types.GlacierInventoryRetrievalJobOutput;
import bglutil.common.types.GlacierInventoryRetrievalParameters;
import bglutil.common.types.SNSNotificationMessageGlacierJobRetrieveInventory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.glacier.AmazonGlacier;
import com.amazonaws.services.glacier.model.DeleteArchiveRequest;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.GetJobOutputRequest;
import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.InventoryRetrievalJobInput;
import com.amazonaws.services.glacier.model.JobParameters;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

public class GlacierUtil implements IUtil{
	
	public void printAllPhysicalId(Object glacier){
		for(DescribeVaultOutput voutput:((AmazonGlacier)glacier).listVaults(new ListVaultsRequest().withAccountId("-")).getVaultList()){
			System.out.println("glacier-vault: "+voutput.getVaultName()+", archives: "+voutput.getNumberOfArchives());
		}
	}
	
	// Glacier
	public GlacierInventoryRetrievalParameters evaludateSNSGlacierInventoryRetrievalParameters(String jsonString){
		JsonNode n = GeneralUtil.getJsonNode(jsonString);
		return new GlacierInventoryRetrievalParameters(
					n.get("EndDate").asText(),
					n.get("Format").asText(),
					n.get("Limit").asLong(),
					n.get("Marker").asText(),
					n.get("StartDate").asText()
				);
	}
	
	public SNSNotificationMessageGlacierJobRetrieveInventory evaluateSNSMessageGlacierJobRetrieveInventory(String jsonString){
		JsonNode n = GeneralUtil.getJsonNode(jsonString);
		return new SNSNotificationMessageGlacierJobRetrieveInventory(
					n.get("Action").asText(),
					n.get("ArchiveId").asText(),
					n.get("ArchiveSHA256TreeHash").asText(),
					n.get("ArchiveSizeInBytes").asLong(),
					n.get("Completed").asText(),
					n.get("CompletionDate").asText(),
					n.get("CreationDate").asText(),
					n.get("InventoryRetrievalParameters").toString(),
					n.get("InventorySizeInBytes").asLong(),
					n.get("JobDescription").asText(),
					n.get("JobId").asText(),
					n.get("RetrievalByteRange").asLong(),
					n.get("SHA256TreeHash").asText(),
					n.get("SNSTopic").asText(),
					n.get("StatusCode").asText(),
					n.get("StatusMessage").asText(),
					n.get("VaultARN").asText()
				);
	}
	
	public GlacierInventoryRetrievalJobOutput evaludateGlacierInventoryRetrievalJobOuptut(String jsonString){
		JsonNode n = GeneralUtil.getJsonNode(jsonString);
		return new GlacierInventoryRetrievalJobOutput(
					n.get("VaultARN").asText(),
					n.get("InventoryDate").asText(),
					n.get("ArchiveList").toString());
	}
	
	public List<GlacierArchive> evaludateGlacierArchives(String jsonString){
		JsonNode n = GeneralUtil.getJsonNode(jsonString);
		Iterator<JsonNode> archives = n.elements();
		ArrayList<GlacierArchive> retArchives = new ArrayList<GlacierArchive>();
		JsonNode c = null;
		while(archives.hasNext()){
			c = archives.next();
			retArchives.add(
						new GlacierArchive(
									c.get("ArchiveId").asText(),
									c.get("ArchiveDescription").asText(),
									c.get("CreationDate").asText(),
									c.get("Size").asLong(),
									c.get("SHA256TreeHash").asText()
								)
					);
		}
		return retArchives;
	}
	
	public void removeArchviesByRetrievalJobId(AmazonGlacier glacier, String vaultName, String jobId) throws IOException{
		InputStream is = glacier.getJobOutput(new GetJobOutputRequest().withAccountId("-").withJobId(jobId).withVaultName(vaultName)).getBody();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = br.readLine();
		while(line!=null){
			sb.append(line);
			line = br.readLine();
		}
		br.close();
		String output = new String(sb);
		String archiveList = this.evaludateGlacierInventoryRetrievalJobOuptut(output).getArchiveList();
		List<GlacierArchive> archives = this.evaludateGlacierArchives(archiveList);
		for(GlacierArchive ga:archives){
			System.out.println("#######################################################");
			System.out.println("Archive Descritpion: "+ga.getArchiveDescription()+" at "+ga.getCreationDate());
			try{
				this.deleteArchive(glacier, vaultName, ga.getArchiveId());
			}catch(AmazonClientException ex){
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public void printGlacierJobOutput(AmazonGlacier glacier, String vaultName, String jobId) throws IOException{
		InputStream is = glacier.getJobOutput(new GetJobOutputRequest().withAccountId("-").withJobId(jobId).withVaultName(vaultName)).getBody();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = br.readLine();
		while(line!=null){
			sb.append(line);
			System.out.println(line);
			line = br.readLine();
		}
		br.close();
		String output = new String(sb);
		String archiveList = this.evaludateGlacierInventoryRetrievalJobOuptut(output).getArchiveList();
		List<GlacierArchive> archives = this.evaludateGlacierArchives(archiveList);
		for(GlacierArchive ga:archives){
			System.out.println("#######################################################");
			System.out.println("Archive Descritpion: "+ga.getArchiveDescription()+" at "+ga.getCreationDate());
			System.out.println(vaultName+" "+ga.getArchiveId());
		}
	}
	
	public String retrieveInventory(AmazonGlacier glacier, String vaultName, String snsTopic){
		Calendar endCal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String endDate = dateFormat.format(endCal.getTime());
		String type = "inventory-retrieval";
		JobParameters jp = new JobParameters()
							.withDescription("Biu "+type+"in vault "+vaultName+" at "+new Date().toString())
							.withFormat("JSON")
							.withType(type)
							.withSNSTopic(snsTopic)
							.withInventoryRetrievalParameters(new InventoryRetrievalJobInput()
																.withLimit("100")
																.withEndDate(endDate));
		InitiateJobRequest request = new InitiateJobRequest()
										.withAccountId("-")
										.withVaultName(vaultName)
										.withJobParameters(jp);
		System.out.println("Retrieval job status will be published to "+snsTopic);
		return glacier.initiateJob(request).getLocation();
	}
	
	public void deleteArchive(AmazonGlacier glacier, String vaultName, String archiveId){
		glacier.deleteArchive(new DeleteArchiveRequest().withAccountId("-").withVaultName(vaultName).withArchiveId(archiveId));
		System.out.println("=> Deleting archive: "+archiveId);
	}
	
}
