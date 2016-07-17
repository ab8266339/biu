package bglutil.common.kinesis;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;

public class KCLRecordsPrinterFactory implements IRecordProcessorFactory{
	
	@Override
	public IRecordProcessor createProcessor() {
		return new KCLRecordsPrinterV2();
	}

}
