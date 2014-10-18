package com.simple.orchestrator.api.conf;

import java.util.List;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.simple.orchestrator.api.dataprovider.IDataProvider;
import com.simple.orchestrator.api.hadoop.operation.IOperation;
import com.simple.orchestrator.api.hadoop.operation.IOperationInput;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;


@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "@class")
@JsonSubTypes({ @JsonSubTypes.Type(value = HadoopOperationJobConfiguration.class, name="operation")})
public interface IHadoopJobConfiguration {

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
	List<? extends IOperationInput> getUserInputs();

	/**
	 * The operation for the job
	 * @return
	 */
	IOperation getMapperOperation();
	
	/**
	 * The operation that will be run during the reducer phase.
	 * @return
	 */
	IOperation getReducerOperation();

	/**
	 * List of data providers for the job.
	 * @return
	 */
	List<? extends IDataProvider> getDataProviders();
}
