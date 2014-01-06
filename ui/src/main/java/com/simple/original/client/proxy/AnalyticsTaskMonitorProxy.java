package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.Monitor;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

/**
 * @author nallaraj
 * @description This is the proxy for AlertDefintion domain class 
 */
@ProxyFor(value = Monitor.class, locator = RequestFactoryEntityLocator.class)
public interface AnalyticsTaskMonitorProxy extends DatastoreObjectProxy {
   
    public static final String[] EDIT_PROPERTIES = {"analyticsTask", "owner", "output", "subscribers" };
    
	
	public static final String[] TASK_PROPERTIES = { "analyticsTask" };

	/**
	 * getter of alert names
	 * @return alert names
	 */
	public String getName();

	/**
	 * Setter method for alert names
	 * @param alertName
	 */
	public void setName(String alertName);

	/**
	 * getter of alertDescription
	 * @return alertDescription
	 */
	public String getDescription();

	/**
	 * Setter method for alertDescription
	 * @param alertDescription
	 */
	public void setDescription(String alertDescription);

	/**
	 * getter of alertStatus
	 * @return AlertStatus
	 */
	public Boolean getAlertStatus();

	/**
	 * Setter method for alertStatus
	 * @param alertStatus
	 */
	public void setAlertStatus(Boolean alertStatus);

	/**
	 * getter for Task 
	 * @return AnalyticsTaskProxy
	 */
	public AnalyticsTaskProxy getAnalyticsTask();

	/**
	 * Setter method for task
	 * @param task
	 */
	public void setAnalyticsTask(AnalyticsTaskProxy analyticsTask);

	/**
	 * getter for subscribers
	 * @return List<PersonProxy>
	 */
	public List<PersonProxy> getSubscribers();

	/**
	 * Setter method for subscribers
	 * @param subscribers
	 */
	public void setSubscribers(List<PersonProxy> subscribers);

	/**
     * getter method for owner
     * @return
     */
    public PersonProxy getOwner();

	/**
     * getter method for public
     * @return
     */
    public boolean isPublic();

    /**
     * setter method for public
     * @return
     */
    public void setPublic(boolean isPublic);

    /**
     * setter for quix enabled. If this is true a quix case will
     * be created when this alert is fired.
     * @param quixEnabled
     */
    public void setQuixEnabled(boolean quixEnabled);
    
    /**
     * Is quix enabled for this alert definition. If true then a quix
     * case will be created when the alert is fired.
     * @return
     */
    public boolean isQuixEnabled();

    
	/**
	 * Get the monitored output from the operation
	 * @return
	 */
    public AnalyticsOperationOutputProxy getOutput();
	
	/**
	 * Setter for the monitored output
	 * @param metric
	 */
	public void setOutput(AnalyticsOperationOutputProxy output);
}
