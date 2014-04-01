package com.simple.original.client.service.rest;

import java.util.List;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.IOperationJob;

/**
 * This is a wrapper for the 
 * @author chris
 */
public class OperationJob implements IOperationJob {

	private String owner;
	
	private AnalyticsOperation operation;
	
	private List<AnalyticsOperationInput> userInputs;
	
	private List<DataProvider> dataProviders;
	
	@Override
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public List<AnalyticsOperationInput> getUserInputs() {
		return userInputs;
	}

	public void setUserInputs(List<AnalyticsOperationInput> userInputs) {
		this.userInputs = userInputs;
	}
	
	@Override
	public AnalyticsOperation getOperation() {
		return operation;
	}
	
	public void setOperation(AnalyticsOperation operation) {
		this.operation = operation;
	}

	@Override
	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}
}
