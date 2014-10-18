package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.ui.DataProviderInput;
import com.simple.orchestrator.api.dataprovider.IDataProviderInput;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

/**
 * This is a proxy for the data provider input. This is not the data provider
 * but a description of a required data provider for an AnalyticsOperation.
 * 
 * @author chris
 */
@ProxyFor(value = DataProviderInput.class, locator = RequestFactoryEntityLocator.class)
public interface DataProviderInputProxy extends DatastoreObjectProxy {

	/**
	 * Get type of data provider.
	 * @return
	 */
	public IDataProviderInput.Type getType();

	/**
	 * This is the type of the data provider.
	 * @param type
	 */
	public void setType(IDataProviderInput.Type type);

	/**
	 * Get a text description of the dataprovider
	 * @return The text description of the data provider.
	 */
	public String getDescription();

	/**
	 * Set the text description of the data provider.
	 * @param description The description
	 */
	public void setDescription(String description);
}
