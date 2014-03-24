package com.simple.engine.api;

import java.util.List;

import com.simple.api.orchestrator.IAnalyticsOperation;
import com.simple.api.orchestrator.IAnalyticsOperationInput;
import com.simple.api.orchestrator.IDataProvider;

public interface IOperationJob {

	/**
	 * Unique key for the job owner
	 * 
	 * @return
	 */
	public String getOwner();

	/**
	 * List of user inputs for the job
	 * @return
	 */
	List<? extends IAnalyticsOperationInput> getUserInputs();

	/**
	 * The operation for the job
	 * @return
	 */
	IAnalyticsOperation getOperation();

	/**
	 * List of data providers for the job.
	 * @return
	 */
	List<? extends IDataProvider> getDataProviders();

}
