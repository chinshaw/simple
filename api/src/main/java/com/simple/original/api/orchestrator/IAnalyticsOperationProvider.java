package com.simple.original.api.orchestrator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface IAnalyticsOperationProvider extends Serializable {

    public Map<String, IMetric> execute(IAnalyticsOperation operation, List<IAnalyticsOperationInput> inputs, List<? extends IDataProvider> dataProvider) throws Exception;
	
    public void interrupt() throws Exception;
    
	public abstract void close() throws Exception;
}