package com.simple.orchestrator.api.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.dataprovider.IDataProvider;
import com.simple.orchestrator.api.hadoop.operation.OperationInput;
import com.simple.orchestrator.api.hadoop.operation.IOperation;


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

	private List<OperationInput> inputs;

	private List<IDataProvider> dataProviders;

	private IOperation mapperOperation;

	private IOperation reducerOperation;

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

	public List<OperationInput> getUserInputs() {
		return inputs;
	}

	public List<OperationInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<OperationInput> inputs) {
		this.inputs = inputs;
	}

	public List<IDataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<IDataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}

	public IOperation getMapperOperation() {
		return mapperOperation;
	}

	public void setMapperOperation(IOperation mapperOperation) {
		this.mapperOperation = mapperOperation;
	}

	public IOperation getReducerOperation() {
		return reducerOperation;
	}

	public void setRecducerOperation(IOperation reducerOperation) {
		this.reducerOperation = reducerOperation;
	}

	/**
	 * Simple buidler class to make creating a Hadoop Job configuration
	 * more straight forward. 
	 * @author chinshaw
	 *
	 */
	public static final class Builder {

		private IOperation mapperOperation;

		private IOperation reducerOperation;

		private List<IDataProvider> dataProviders;

		private List<OperationInput> inputs;

		public Builder setMapperOperation(IOperation mapperOperation) {
			this.mapperOperation = mapperOperation;
			return this;
		}

		public Builder setReducerOperation(IOperation reducerOperation) {
			this.reducerOperation = reducerOperation;
			return this;
		}

		public Builder setDataProviders(List<IDataProvider> dataProviders) {
			this.dataProviders = dataProviders;
			return this;
		}

		public Builder addDataProvider(IDataProvider dataProvider) {
			if (this.dataProviders == null) {
				this.dataProviders = new ArrayList<IDataProvider>();
			}
			this.dataProviders.add(dataProvider);
			return this;

		}

		public Builder setUserInputs(List<OperationInput> inputs) {
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
