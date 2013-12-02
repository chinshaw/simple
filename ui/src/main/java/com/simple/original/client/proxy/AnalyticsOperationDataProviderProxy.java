package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.DataProvider;

/**
 * This is an interface to the analytics data provider class which provides a
 * way to describe what data providers are needed for an analytics operation.
 * This can be used in validation of the analytics operation.
 * 
 * @author chinshaw
 */
@ProxyFor(DataProvider.class)
public interface AnalyticsOperationDataProviderProxy extends ValueProxy {

    /**
     * Getter for the variable name of the provider.
     * 
     * @return
     */
    public String getVariableName();

    /**
     * Setter for the variable name of the data provider
     * 
     * @param name
     */
    public void setVariableName(String name);

    /**
     * Gets the description of the required data provider.
     * 
     * @return
     */
    public String getDescription();

    /**
     * Sets a description for the required data provider.
     * 
     * @param description
     */
    public void setDescription(String description);

}
