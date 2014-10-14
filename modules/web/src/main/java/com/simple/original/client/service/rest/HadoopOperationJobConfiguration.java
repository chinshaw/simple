package com.simple.original.client.service.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.simple.api.orchestrator.IAnalyticsOperation;
import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;

/**
 * This is a wrapper for the 
 * @author chris
 */
@XmlRootElement
public class HadoopOperationJobConfiguration implements IHadoopJobConfiguration {

	private String owner;
	
	private AnalyticsOperation mapperOperation;
	
	private AnalyticsOperation reducerOperation;
	
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
	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}

	@Override
	public IAnalyticsOperation getMapperOperation() {
		return mapperOperation;
	}

	@Override
	public IAnalyticsOperation getReducerOperation() {
		return reducerOperation;
	}
}
