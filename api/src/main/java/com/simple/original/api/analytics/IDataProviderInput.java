package com.simple.original.api.analytics;

import javax.xml.bind.annotation.XmlType;

/**
 * Data provider input is an input that is used to specify what input 
 * will be sent to an operation, these include DB, FILE or WEB input.
 * @author chris
 */
public interface IDataProviderInput {

	/**
	 * The type of input for the data provider.
	 * @author chris
	 *
	 */

	@XmlType(name="dataprovider_type")
	enum Type {
		DB,
		FILE,
		WEB;
	}

	public String getDescription();
	
	public Type getType();
}