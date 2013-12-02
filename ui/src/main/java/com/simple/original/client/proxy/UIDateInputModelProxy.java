package com.simple.original.client.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.DateInput;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = DateInput.class, locator=RequestFactoryEntityLocator.class)
public interface UIDateInputModelProxy extends AnalyticsOperationInputProxy {

    public Date getValue();

    public void setValue(Date value);
}
