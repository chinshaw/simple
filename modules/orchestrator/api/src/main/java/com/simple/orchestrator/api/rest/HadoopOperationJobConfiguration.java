package com.simple.orchestrator.api.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.hadoop.operation.IHadoopOperation;


/**
 * This is the job configuration for a specific job, this will inlude
 * the operation for both the mapper and reducer phases. It also contains
 * a builder to hlep build the configuration more simply.
 * 
 * @author chinshaw
 */
@XmlRootElement
public class HadoopOperationJobConfiguration implements
		IHadoopJobConfiguration {

	private String owner;

	private List<AnalyticsOperationInput> inputs;

	private List<DataProvider> dataProviders;

	private IHadoopOperation mapperOperation;

	private IHadoopOperation reducerOperation;

	/**
	 * 
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

	public List<AnalyticsOperationInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<AnalyticsOperationInput> inputs) {
		this.inputs = inputs;
	}

	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}

	public IHadoopOperation getMapperOperation() {
		return mapperOperation;
	}

	public void setMapperOperation(IHadoopOperation mapperOperation) {
		this.mapperOperation = mapperOperation;
	}

	public IHadoopOperation getReducerOperation() {
		return reducerOperation;
	}

	public void setRecducerOperation(IHadoopOperation reducerOperation) {
		this.reducerOperation = reducerOperation;
	}

	/**
	 * Simple buidler class to make creating a Hadoop Job configuration
	 * more straight forward. 
	 * @author chinshaw
	 *
	 */
	public static final class Builder {

		private IHadoopOperation mapperOperation;

		private IHadoopOperation reducerOperation;

		private List<DataProvider> dataProviders;

		private List<AnalyticsOperationInput> inputs;

		public Builder setMapperOperation(IHadoopOperation mapperOperation) {
			this.mapperOperation = mapperOperation;
			return this;
		}

		public Builder setReducerOperation(IHadoopOperation reducerOperation) {
			this.reducerOperation = reducerOperation;
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

		public IHadoopJobConfiguration build() {
			HadoopOperationJobConfiguration config = new HadoopOperationJobConfiguration();
			config.setInputs(inputs);
			config.setDataProviders(dataProviders);
			config.setMapperOperation(mapperOperation);
			config.setRecducerOperation(reducerOperation);
			return config;
		}
	}
}
