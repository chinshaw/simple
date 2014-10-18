package com.simple.orchestrator.api.hadoop.operation;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.simple.orchestrator.api.hadoop.operation.IOperationInput.Adapter;

@XmlJavaTypeAdapter(Adapter.class)
public interface IOperationInput {
	
	
	
	
	static class Adapter extends XmlAdapter<AbstractOperationInput, IOperationInput> {

		@Override
		public IOperationInput unmarshal(AbstractOperationInput v) throws Exception {
			return v;
		}

		@Override
		public AbstractOperationInput marshal(IOperationInput v) throws Exception {
			return (AbstractOperationInput) v;
		}

	}
}
