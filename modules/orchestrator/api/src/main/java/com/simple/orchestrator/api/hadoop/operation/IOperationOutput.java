package com.simple.orchestrator.api.hadoop.operation;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.simple.orchestrator.api.hadoop.operation.IOperationOutput.Adapter;

@XmlJavaTypeAdapter(Adapter.class)
public interface IOperationOutput {

	static class Adapter extends XmlAdapter<AbstractOperationOutput, IOperationOutput> {

		@Override
		public IOperationOutput unmarshal(AbstractOperationOutput v) throws Exception {
			return v;
		}

		@Override
		public AbstractOperationOutput marshal(IOperationOutput v) throws Exception {
			return (AbstractOperationOutput) v;
		}

	}
}
