package com.simple.orchestrator.api.dataprovider;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.simple.orchestrator.api.dataprovider.IDataProvider.Adapter;

@XmlJavaTypeAdapter(Adapter.class)
public interface IDataProvider {

	/**
	 * Getter for variable name.
	 * 
	 * @return variable name that will be assigned the value.
	 */
	public abstract String getVariableName();

	/**
	 * Setter for variable name.
	 * 
	 * @param variableName
	 */
	public abstract void setVariableName(String variableName);

	static class Adapter extends XmlAdapter<AbstractDataProvider, IDataProvider> {

		@Override
		public IDataProvider unmarshal(AbstractDataProvider v) throws Exception {
			return v;
		}

		@Override
		public AbstractDataProvider marshal(IDataProvider v) throws Exception {
			return (AbstractDataProvider) v;
		}

	}
}