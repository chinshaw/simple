package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.AnalyticsTask;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = AnalyticsTask.class, locator = RequestFactoryEntityLocator.class)
public interface AnalyticsTaskProxy extends DatastoreObjectProxy {

    public static final String[] EXEC_PROPERTIES = { "analyticsOperations", "analyticsOperations.inputs", "analyticsOperations.inputs.inputs"};

    public static final String[] EDIT_PROPERTIES = { "analyticsOperations", "dataProviders", "dashboard" ,"owner"};

    public static final String[] DESIGNER_PROPERTIES = { "analyticsOperations", "analyticsOperations.outputs" };


    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public List<AnalyticsOperationProxy> getAnalyticsOperations();

    public void setAnalyticsOperations(List<AnalyticsOperationProxy> analyticsOperations);

    /**
     * List of all data providers for this analytics task.
     * 
     * @return
     */
    public List<DataProviderProxy> getDataProviders();

    /**
     * Setter for all data providers.
     * 
     * @param dataProviders
     */
    public void setDataProviders(List<DataProviderProxy> dataProviders);

    public boolean isPublic();

    public void setPublic(boolean isPublic);

	public PersonProxy getOwner();
	
	public DashboardProxy getDashboard();
	
	public List<AnalyticsOperationInputProxy> getTaskInputs();

	public void setTaskInputs(List<AnalyticsOperationInputProxy> inputs);
	
}
