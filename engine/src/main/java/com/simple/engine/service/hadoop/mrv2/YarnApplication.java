package com.simple.engine.service.hadoop.mrv2;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.simple.api.exceptions.RAnalyticsException;
import com.simple.api.orchestrator.IAnalyticsOperation;
import com.simple.api.orchestrator.IMetric;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.engine.service.AnalyticsOperationException;
import com.simple.engine.service.IAnalyticsOperationExecutor;

public class YarnApplication implements IAnalyticsOperationExecutor {

	private static final Logger logger = Logger.getLogger(YarnApplication.class.getName());
	
	@Override
	public HashMap<Long, IMetric> execute(String jobOwner,
			List<AnalyticsOperationInput> userInputs,
			IAnalyticsOperation operation, List<DataProvider> dataProviders)
			throws AnalyticsOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(String jobOwner, IAnalyticsOperation operation,
			List<DataProvider> dataProvider) throws AnalyticsOperationException {
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
