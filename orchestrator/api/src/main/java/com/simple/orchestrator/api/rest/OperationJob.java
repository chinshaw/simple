package com.simple.orchestrator.api.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.IOperationJob;

@XmlRootElement
public class OperationJob implements IOperationJob {

	private String owner;

	private List<AnalyticsOperationInput> inputs;

	private List<DataProvider> dataProviders;

	private AnalyticsOperation operation;

	
	public OperationJob() {
		
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<AnalyticsOperationInput> getUserInputs() {
		return inputs;
	}

	public void setInputs(List<AnalyticsOperationInput> inputs) {
		this.inputs = inputs;
	}

	public AnalyticsOperation getOperation() {
		return operation;
	}

	public void setOperation(AnalyticsOperation operation) {
		this.operation = operation;
	}

	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}
}
