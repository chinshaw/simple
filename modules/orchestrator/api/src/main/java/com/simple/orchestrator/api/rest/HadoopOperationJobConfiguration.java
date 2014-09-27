package com.simple.orchestrator.api.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.AbstractMetricMapper;
import com.simple.orchestrator.api.AbstractMetricReducer;
import com.simple.orchestrator.api.IHadoopOperationJobConfiguration;

@XmlRootElement
public class HadoopOperationJobConfiguration implements IHadoopOperationJobConfiguration {

	private String owner;

	private List<AnalyticsOperationInput> inputs;

	private List<DataProvider> dataProviders;

	private AnalyticsOperation operation;
	
	private AbstractMetricMapper<?,?,?,?> mapper;
	
	private AbstractMetricReducer<?,?,?,?> reduer;

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
	
	public AbstractMetricMapper<?, ?, ?, ?> getMapper() {
		return mapper;
	}

	public void setMapper(AbstractMetricMapper<?, ?, ?, ?> mapper) {
		this.mapper = mapper;
	}

	public AbstractMetricReducer<?, ?, ?, ?> getReduer() {
		return reduer;
	}

	public void setReduer(AbstractMetricReducer<?, ?, ?, ?> reduer) {
		this.reduer = reduer;
	}

	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}
	
	public static final class Builder {
		
		private AnalyticsOperation operation;
		
		private AbstractMetricMapper<?,?,?,?> mapper;
		
		private AbstractMetricReducer<?,?,?,?> reducer;
		
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
			config.setMapper(mapper);
			config.setReduer(reducer);
			return config;
		}

		/**
		 * @param mapper
		 * @return
		 */
		public Builder setMapper(AbstractMetricMapper<?, ?, ?, ?> mapper) {
			this.mapper = mapper;
			return this;
		}

		/**
		 * Add an abstract reducer to the configuration. This will be used during
		 * the reduce phase.
		 * 
		 * @param reducer
		 * @return
		 */
		public Builder setReducer(AbstractMetricReducer<?,?,?,?> reducer) {
			this.reducer = reducer;
			return this;
		}
	}
}
