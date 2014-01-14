package com.simple.engine.service.hadoop;

import java.util.List;

import javax.xml.bind.JAXBException;
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
	
	
	public OperationConfig() {
		super();
	}
	
	public OperationConfig(Configuration configuration) {
		super(configuration);
	}
	
	public List<DataProvider> getDataProviders() throws JAXBException {
		String xml = get(DATA_PROVIDER_PARAMS);
		if (xml == null || xml.isEmpty()) {
			return null;
		}
		
		DPWrapper wrapper = ConfigurationUtils.unserializeXml(DPWrapper.class, xml);
		return wrapper.dataProviders;
	}
	
	public void setDataProviders(List<DataProvider> dataProviders) throws JAXBException {
		set(DATA_PROVIDER_PARAMS, ConfigurationUtils.serializeToXml(DPWrapper.class, new DPWrapper(dataProviders)));
	}
	
	public List<AnalyticsOperationInput> getOperationInputs() throws JAXBException {
		String xml = get(OPERATION_INPUT_PARAMS);
		INWrapper wrapper = ConfigurationUtils.unserializeXml(INWrapper.class, xml);
		return wrapper.inputs;
	}
	
	public void setOperationInputs(List<AnalyticsOperationInput> inputs) throws JAXBException {
		set(OPERATION_INPUT_PARAMS, ConfigurationUtils.serializeToXml(INWrapper.class, new INWrapper(inputs)));
	}
	
	public AnalyticsOperation getOperation() throws JAXBException {
		String xml = get(OPERATION);
		return ConfigurationUtils.unserializeXml(AnalyticsOperation.class, xml);
	}
	
	public void setOperation(AnalyticsOperation operation) throws JAXBException {
		set(OPERATION, ConfigurationUtils.serializeToXml(AnalyticsOperation.class, operation));
	}

}