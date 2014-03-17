package com.simple.original.api.orchestrator;

import java.util.List;

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
	List<IAnalyticsOperationInput> getUserInputs();

	/**
	 * The operation for the job
	 * @return
	 */
	IAnalyticsOperation getOperation();

	/**
	 * List of data providers for the job.
	 * @return
	 */
	List<IDataProvider> getDataProviders();

}
