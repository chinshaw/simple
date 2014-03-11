package com.simple.engine.api;

import java.util.List;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;

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
	List<AnalyticsOperationInput> getUserInputs();

	/**
	 * The operation for the job
	 * @return
	 */
	AnalyticsOperation getOperation();

	/**
	 * List of data providers for the job.
	 * @return
	 */
	List<DataProvider> getDataProviders();

}
