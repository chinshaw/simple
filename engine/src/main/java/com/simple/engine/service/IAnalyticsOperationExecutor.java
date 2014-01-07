package com.simple.engine.service;

import java.util.HashMap;
import java.util.List;

import com.simple.domain.AnalyticsOperation;
import com.simple.domain.AnalyticsOperationInput;
import com.simple.domain.DataProvider;
import com.simple.domain.metric.Metric;
import com.simple.original.api.exceptions.RAnalyticsException;

public interface IAnalyticsOperationExecutor {

    public abstract HashMap<Long, Metric>  execute(List<AnalyticsOperationInput> userInputs, AnalyticsOperation operation, List<DataProvider> dataProviders) throws AnalyticsOperationException;
    
    public abstract void execute(AnalyticsOperation operation, List<DataProvider> dataProvider) throws AnalyticsOperationException;

    public abstract boolean isOperationSuccessful();

	public abstract void reset() throws RAnalyticsException;

	public abstract void close();
}