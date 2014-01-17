package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

/**
 * The goal is to later extract away the implementation of RSQL Data provder to
 * be an interface proxy for would be great as value="DataProvider.class" where
 * DataProvider.class in an actual interface and not an implementation TODO Fix
 * this class to use an interface so that dataprovider could be any kind of
 * provider defined in DataProvider interface
 * 
 * @author chinshaw
 * 
 */
@ProxyFor(value = DataProvider.class, locator = RequestFactoryEntityLocator.class)
public interface DataProviderProxy extends ValueProxy {
    
    public enum DataProviderType {
        SQL, R_Command
    }

    public String getVariableName();

    public void setVariableName(String variableName);
}
