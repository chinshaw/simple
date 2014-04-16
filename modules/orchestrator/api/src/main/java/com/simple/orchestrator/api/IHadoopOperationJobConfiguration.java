package com.simple.orchestrator.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.simple.api.orchestrator.IAnalyticsOperation;
import com.simple.api.orchestrator.IAnalyticsOperationInput;
import com.simple.api.orchestrator.IDataProvider;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;


@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "@class")
@JsonSubTypes({ @JsonSubTypes.Type(value = HadoopOperationJobConfiguration.class, name="operation")})
public interface IHadoopOperationJobConfiguration {

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
