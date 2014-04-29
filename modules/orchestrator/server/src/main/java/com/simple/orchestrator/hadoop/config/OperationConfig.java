package com.simple.orchestrator.hadoop.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.hadoop.conf.Configuration;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;

public class OperationConfig extends Configuration {

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	static class DPWrapper {
		List<DataProvider> dataProviders;
		
		public DPWrapper() {
			
		}
		public DPWrapper(List<DataProvider> dataProviders) {
			this.dataProviders = dataProviders;
		}
	}
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	static class INWrapper {
		
		List<AnalyticsOperationInput> inputs;
		
		public INWrapper() {
			
		}
		public INWrapper(List<AnalyticsOperationInput> inputs) {
			this.inputs = inputs;
		}
		
		public List<AnalyticsOperationInput> getInputs() {
			return inputs;
		}
	}
	
	
	public static final String OPERATION = "mapreduce.artisan.operation";
	
	public static final String DATA_PROVIDER_PARAMS = "mapreduce.artisan.dataproviders";
	
	public static final String OPERATION_INPUT_PARAMS = "mapreduce.artisan.operationinputs";
	
	
	public static List<DataProvider> getDataProviders(Configuration conf) throws ConfigurationException {
		String xml = conf.get(DATA_PROVIDER_PARAMS);
		if (xml == null || xml.isEmpty()) {
			return null;
		}
		
		DPWrapper wrapper = ConfigurationUtils.unserializeXml(DPWrapper.class, xml);
		return wrapper.dataProviders;
	}
	
	public static void setDataProviders(Configuration conf, List<DataProvider> dataProviders) throws ConfigurationException {
		conf.set(DATA_PROVIDER_PARAMS, ConfigurationUtils.serializeToXml(DPWrapper.class, new DPWrapper(dataProviders)));
	}
	
	public static List<AnalyticsOperationInput> getOperationInputs(Configuration conf) throws ConfigurationException {
		String xml = conf.get(OPERATION_INPUT_PARAMS);
		INWrapper wrapper = ConfigurationUtils.unserializeXml(INWrapper.class, xml);
		return wrapper.inputs;
	}
	
	public  static void setOperationInputs(Configuration conf, List<AnalyticsOperationInput> inputs) throws ConfigurationException {
		conf.set(OPERATION_INPUT_PARAMS, ConfigurationUtils.serializeToXml(INWrapper.class, new INWrapper(inputs)));
	}
	
	public static AnalyticsOperation getOperation(Configuration conf) throws ConfigurationException {
		String xml = conf.get(OPERATION);
		if (xml == null) {
			throw new ConfigurationException("operation xml == null");
		}
		return ConfigurationUtils.unserializeXml(AnalyticsOperation.class, xml);
	}
	
	public static void setOperation(Configuration conf, AnalyticsOperation operation) throws ConfigurationException {
		conf.set(OPERATION, ConfigurationUtils.serializeToXml(AnalyticsOperation.class, operation));
	}
}