package com.simple.original.client.service.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.dataprovider.IDataProvider;
import com.simple.orchestrator.api.hadoop.operation.IOperation;
import com.simple.orchestrator.api.hadoop.operation.IOperationInput;
import com.simple.orchestrator.api.hadoop.operation.IOperationOutput;

/**
 * This is a wrapper for the 
 * @author chris
 */
@XmlRootElement
public class HadoopOperationJobConfiguration implements IHadoopJobConfiguration {

	private String owner;
	
	private IOperation mapperOperation;
	
	private IOperation reducerOperation;
	
	private List<IOperationInput> userInputs;
	
	private List<IDataProvider> dataProviders;
	
	@Override
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public List<IOperationInput> getUserInputs() {
		return userInputs;
	}

	public void setUserInputs(List<IOperationInput> userInputs) {
		this.userInputs = userInputs;
	}
	
	@Override
	public List<IDataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<IDataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}

	@Override
	public IOperation getMapperOperation() {
		return mapperOperation;
	}

	@Override
	public IOperation getReducerOperation() {
		return reducerOperation;
	}
}
