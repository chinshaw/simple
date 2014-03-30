package com.simple.orchestrator.service.hadoop.job;

import java.util.List;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.IOperationJob;

public class OperationJob implements IOperationJob {

	public String owner;

	public List<AnalyticsOperationInput> inputs;

	public List<DataProvider> dataProviders;

	public AnalyticsOperation operation;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<AnalyticsOperationInput> getInputs() {
		return inputs;
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
