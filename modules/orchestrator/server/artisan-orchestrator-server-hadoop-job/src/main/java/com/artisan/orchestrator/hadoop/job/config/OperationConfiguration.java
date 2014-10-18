package com.artisan.orchestrator.hadoop.job.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.hadoop.conf.Configuration;

import com.simple.orchestrator.api.conf.ConfigurationException;
import com.simple.orchestrator.api.dataprovider.IDataProvider;
import com.simple.orchestrator.api.hadoop.operation.Operation;
import com.simple.orchestrator.api.hadoop.operation.IOperation;
import com.simple.orchestrator.api.hadoop.operation.IOperationInput;

public class OperationConfiguration extends Configuration {

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	static class DPWrapper {
		List<IDataProvider> dataProviders;

		public DPWrapper() {

		}

		public DPWrapper(List<IDataProvider> dataProviders) {
			this.dataProviders = dataProviders;
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	static class INWrapper {

		List<IOperationInput> inputs;

		public INWrapper() {

		}

		public INWrapper(List<IOperationInput> inputs) {
			this.inputs = inputs;
		}

		public List<IOperationInput> getInputs() {
			return inputs;
		}
	}

	public static final String MAPPER_OPERATION = "mapreduce.artisan.operation.mapper";

	public static final String REDUCER_OPERATION = "mapreduce.artisan.operation.reducer";

	public static final String DATA_PROVIDER_PARAMS = "mapreduce.artisan.dataproviders";

	public static final String OPERATION_INPUT_PARAMS = "mapreduce.artisan.operationinputs";

	public void setMapperOperation(IOperation operation)
			throws ConfigurationException {
		setMapperOperation(this, operation);
	}

	public void setReducerOperation(IOperation operation)
			throws ConfigurationException {
		setReducerOperation(this, operation);
	}

	public void setDataProviders(List dataProviders)
			throws ConfigurationException {
		setDataProviders(this, dataProviders);
	}

	public void setOperationInputs(List userInputs)
			throws ConfigurationException {
		setOperationInputs(this, userInputs);
	}

	public static List<IDataProvider> getDataProviders(Configuration conf)
			throws ConfigurationException {
		String xml = conf.get(DATA_PROVIDER_PARAMS);
		if (xml == null || xml.isEmpty()) {
			return null;
		}

		DPWrapper wrapper = ConfigurationUtils.unserializeXml(DPWrapper.class,
				xml);
		return wrapper.dataProviders;
	}

	public static void setDataProviders(Configuration conf,
			List<IDataProvider> dataProviders) throws ConfigurationException {
		conf.set(DATA_PROVIDER_PARAMS, ConfigurationUtils.serializeToXml(
				DPWrapper.class, new DPWrapper(dataProviders)));
	}

	public static List<IOperationInput> getOperationInputs(
			Configuration conf) throws ConfigurationException {
		String xml = conf.get(OPERATION_INPUT_PARAMS);
		INWrapper wrapper = ConfigurationUtils.unserializeXml(INWrapper.class,
				xml);
		return wrapper.inputs;
	}

	public static void setOperationInputs(Configuration conf,
			List<IOperationInput> inputs) throws ConfigurationException {
		conf.set(OPERATION_INPUT_PARAMS, ConfigurationUtils.serializeToXml(
				INWrapper.class, new INWrapper(inputs)));
	}

	public static Operation getMapperOperation(Configuration conf)
			throws ConfigurationException {
		String xml = conf.get(MAPPER_OPERATION);
		if (xml == null) {
			throw new ConfigurationException("operation xml == null");
		}
		return ConfigurationUtils.unserializeXml(Operation.class, xml);
	}

	public static Operation getOperation(Configuration conf)
			throws ConfigurationException {
		String xml = conf.get(REDUCER_OPERATION);
		if (xml == null) {
			throw new ConfigurationException("operation xml == null");
		}
		return ConfigurationUtils.unserializeXml(Operation.class, xml);
	}

	public static void setMapperOperation(Configuration conf,
			IOperation operation) throws ConfigurationException {
		conf.set(MAPPER_OPERATION, ConfigurationUtils.serializeToXml(
				Operation.class, operation));
	}

	public static void setReducerOperation(Configuration conf,
			IOperation operation) throws ConfigurationException {
		conf.set(REDUCER_OPERATION, ConfigurationUtils.serializeToXml(
				Operation.class, operation));
	}

}