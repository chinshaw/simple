package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = com.simple.domain.dashboard.Dashboard.class, locator = RequestFactoryEntityLocator.class)
public interface DashboardProxy extends DatastoreObjectProxy {
    
    public String getName();
    
    public void setName(String name);
    
    public String getTitle();
    
    public void setTitle(String title);
    
    public String getDescription();
    
    public void setDescription(String description);

    public AnalyticsTaskProxy getAnalyticsTask();

    public void setAnalyticsTask(AnalyticsTaskProxy analtyicsTask);
  
    public String getWidgetModel();
    
    public void setWidgetModel(String descriptor);

	public List<AnalyticsOperationInputProxy> getInputs();
}
