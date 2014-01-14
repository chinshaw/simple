package com.simple.engine.service.hadoop;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.util.ToolRunner;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.IAnalyticsOperationExecutor;
import com.simple.original.api.exceptions.RAnalyticsException;

public class OperationExecutor implements IAnalyticsOperationExecutor {

	
	
	public OperationExecutor() {
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

	private synchronized HashMap<Long, Metric> _execute(List<AnalyticsOperationInput> operationInputs, AnalyticsOperation operation, List<DataProvider> dataProviders)
			throws RAnalyticsException {

		if (!(operation instanceof RAnalyticsOperation)) {
			throw new RAnalyticsException("Invalid operation type");
		}

		RAnalyticsOperation rOperation = (RAnalyticsOperation) operation;
		OperationConfig configuration = new OperationConfig();
		
		try {
			configuration.setOperation(rOperation);
			configuration.setDataProviders(dataProviders);
			configuration.setOperationInputs(operationInputs);
		} catch(JAXBException e) {
			e.printStackTrace();
		}
		
		try {
			String args[] = {};
			
			OperationTool tool = new OperationTool();
			tool.setConf(configuration);
			
			ToolRunner.run(configuration, tool, args);
		} catch (Exception e) {
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
