package com.simple.engine.service.hadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import com.simple.domain.AnalyticsOperation;
import com.simple.domain.AnalyticsOperationInput;
import com.simple.domain.DataProvider;
import com.simple.domain.RAnalyticsOperation;
import com.simple.domain.metric.Metric;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.IAnalyticsOperationExecutor;
import com.simple.original.api.exceptions.RAnalyticsException;

public class Executor implements IAnalyticsOperationExecutor {

	public Executor() {
	}

	@Override
	public HashMap<Long, Metric> execute(List<AnalyticsOperationInput> userInputs, AnalyticsOperation operation, List<DataProvider> dataProviders)
			throws AnalyticsOperationException {

		try {
			return _execute(userInputs, operation, dataProviders);
		} catch (RAnalyticsException e) {
			throw new AnalyticsOperationException("Unable to execute operation " + operation.getName(), e);
		}
	}

	@Override
	public void execute(AnalyticsOperation operation, List<DataProvider> dataProviders) throws AnalyticsOperationException {
	}

	private synchronized HashMap<Long, Metric> _execute(List<AnalyticsOperationInput> userInputs, AnalyticsOperation operation, List<DataProvider> dataProviders)
			throws RAnalyticsException {

		if (!(operation instanceof RAnalyticsOperation)) {
			throw new RAnalyticsException("Invalid operation type");
		}

		RAnalyticsOperation rOperation = (RAnalyticsOperation) operation;

		String serializedOperation = null;
		
		try {
			serializedOperation = JobUtils.serializeObject(rOperation);
		} catch (IOException e) {
			throw new RAnalyticsException("Unable to serialize the operation", e);
		}
		
		Configuration configuration = new Configuration(true);
		//configuration.set(JobUtils.R_OPERATION_PARAM, serializedOperation);
		
		configuration.set(JobUtils.R_OPERATION_CODE, rOperation.getCode());
		String args[] = {};
		
		OperationTool tool = new OperationTool();
		tool.setConf(configuration);
		
		try {
			ToolRunner.run(configuration, tool,args );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}

	@Override
	public boolean isOperationSuccessful() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() throws RAnalyticsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}
}
