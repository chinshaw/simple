package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.ui.ComplexInput;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = ComplexInput.class, locator=RequestFactoryEntityLocator.class)
public interface UIComplexInputModelProxy extends AnalyticsOperationInputProxy {
    
    public abstract List<AnalyticsOperationInputProxy> getInputs();
    
    public abstract void setInputs(List<AnalyticsOperationInputProxy> inputs);
}