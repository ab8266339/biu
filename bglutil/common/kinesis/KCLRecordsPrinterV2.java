package bglutil.common.kinesis;

import java.io.UnsupportedEncodingException;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.KinesisClientLibDependencyException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;

/**
 * There is adapter object can automatically convert V1 object to V2 object, so this v2.IRecordProcessor is NOT used by factory.
 * @author guanglei
 *
 */
public class KCLRecordsPrinterV2 implements IRecordProcessor{
	
	String shardId;
	String data;

	@Override
	public void initialize(InitializationInput ii) {
		this.shardId = ii.getShardId();
		System.out.println("Initalized to handle shard: "+this.shardId);
	}

	@Override
	public void processRecords(ProcessRecordsInput input) {
		try {
			for(Record r:input.getRecords()){
				//ByteBuffer.wrap(String.valueOf(payload).getBytes())
				data = new String(r.getData().array(),"UTF-8");
				System.out.println("C => "+this.shardId+", PartKey: "+r.getPartitionKey()+", Data:"+data+", Seq: "+r.getSequenceNumber());
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			input.getCheckpointer().checkpoint();
		} catch (KinesisClientLibDependencyException e) {
			e.printStackTrace();
		} catch (InvalidStateException e) {
			e.printStackTrace();
		} catch (ThrottlingException e) {
			e.printStackTrace();
		} catch (ShutdownException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown(ShutdownInput si) {
		String chk = "chk no";
		if(si.getShutdownReason().equals(ShutdownReason.TERMINATE)){
			try {
				si.getCheckpointer().checkpoint();
				chk = "chk yes";
			} catch (KinesisClientLibDependencyException e) {
				e.printStackTrace();
			} catch (InvalidStateException e) {
				e.printStackTrace();
			} catch (ThrottlingException e) {
				e.printStackTrace();
			} catch (ShutdownException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Record Processor Shuting down: "+chk+", Shard#: "+this.shardId+" shutdown with reason: "+si.getShutdownReason().toString());
	}
}
