package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.RDataProvider;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;


@ProxyFor(value= RDataProvider.class, locator=RequestFactoryEntityLocator.class)
public interface RDataProviderProxy extends DataProviderProxy {

    void setRCommand(String rCommand);
    
    
    String getRCommand();
    
}
