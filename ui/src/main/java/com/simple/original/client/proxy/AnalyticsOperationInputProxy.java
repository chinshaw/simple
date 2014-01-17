package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = AnalyticsOperationInput.class, locator = RequestFactoryEntityLocator.class)
public interface AnalyticsOperationInputProxy extends ValueProxy {

    public String getDisplayName();

    public void setDisplayName(String displayName);

    public String getInputName();

    public void setInputName(String inputName);

    public Boolean getRequired();

    public void setRequired(Boolean required);
}
