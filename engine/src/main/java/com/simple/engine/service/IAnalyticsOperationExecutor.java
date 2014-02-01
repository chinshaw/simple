package com.simple.engine.service;

import java.util.HashMap;
import java.util.List;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.api.exceptions.RAnalyticsException;

public interface IAnalyticsOperationExecutor {

    /**
     * This is a synchronous execution of an operation. 
     * 
     * @param jobOwner The owner of the job, used for job tracking.
     * @param userInputs List of user inputs for the operation.
     * @param operation The actual operation that will be executed on the cluster.
     * @param dataProviders List of 
     * @return
     * @throws AnalyticsOperationException
     */
	HashMap<Long, Metric>  execute(String jobOwner, List<AnalyticsOperationInput> userInputs, AnalyticsOperation operation, List<DataProvider> dataProviders) throws AnalyticsOperationException;
    
    public abstract void execute(String jobOwner, AnalyticsOperation operation, List<DataProvider> dataProvider) throws AnalyticsOperationException;

    public abstract boolean isOperationSuccessful();

	public abstract void reset() throws RAnalyticsException;

	public abstract void close();
}