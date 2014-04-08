package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.ui.StringInput;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value= StringInput.class, locator=RequestFactoryEntityLocator.class)
public interface UIUserInputModelProxy extends AnalyticsOperationInputProxy {

    
    /**
     * Getter for the input value.
     * 
     * @return
     */
    public String getValue();

    /**
     * Setter for the input value.
     * 
     * @param inputValue
     */
    public void setValue(String value);
    
    
    public List<String> getDefinedInputs();

    public void setDefinedInputs(List<String> definedInputs);
}
