package com.simple.orchestrator.api.rest;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.IHadoopOperationJobConfiguration;

public class HadoopOperationJobConfiguration implements IHadoopOperationJobConfiguration {

	private String owner;

	private List<AnalyticsOperationInput> inputs;

	private List<DataProvider> dataProviders;

	private AnalyticsOperation operation;


	/**
	 * 
	 * @deprecated Use {@link Builder}
	 */
	public HadoopOperationJobConfiguration() {
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

	public void setUserInputs(List<AnalyticsOperationInput> inputs) {
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
	
	public static final class Builder {
		
		private AnalyticsOperation operation;
		
		private List<DataProvider> dataProviders;
		
		private List<AnalyticsOperationInput> inputs;
		
		public Builder setAnalyticsOperation(AnalyticsOperation operation) {
			this.operation = operation;
			return this;
		}
		
		public Builder setDataProviders(List<DataProvider> dataProviders) {
			this.dataProviders = dataProviders;
			return this;
		}
		
		public Builder addDataProvider(DataProvider dataProvider) {
			if (this.dataProviders == null) {
				this.dataProviders = new ArrayList<DataProvider>();
			}
			this.dataProviders.add(dataProvider);
			return this;
			
		}
		
		public Builder setUserInputs(List<AnalyticsOperationInput> inputs) {
			this.inputs = inputs;
			return this;
		}
		
		public IHadoopOperationJobConfiguration build() {
			HadoopOperationJobConfiguration config = new HadoopOperationJobConfiguration();
			config.setOperation(operation);
			config.setDataProviders(dataProviders);
			config.setUserInputs(inputs);
			return config;
		}
	}
}
