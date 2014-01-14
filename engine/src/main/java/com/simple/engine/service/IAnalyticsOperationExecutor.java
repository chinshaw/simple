package com.simple.engine.service;

import java.util.HashMap;
import java.util.List;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.metric.Metric;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.api.exceptions.RAnalyticsException;

public interface IAnalyticsOperationExecutor {

    public abstract HashMap<Long, Metric>  execute(List<AnalyticsOperationInput> userInputs, AnalyticsOperation operation, List<DataProvider> dataProviders) throws AnalyticsOperationException;
    
    public abstract void execute(AnalyticsOperation operation, List<DataProvider> dataProvider) throws AnalyticsOperationException;

    public abstract boolean isOperationSuccessful();

	public abstract void reset() throws RAnalyticsException;

	public abstract void close();
}