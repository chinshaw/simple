package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.ui.dashboard.LinkableDashboard;
import com.simple.original.client.dashboard.ILinkable;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;


/**
 * This is a proxy for the linkable task entity in the datastore. Any child of widget
 * can implement {@link ILinkable} and supply a list of linkabletasks. This will allow a widget to
 * be clickable and send the user to another analytics task when selected. 
 * 
 * @see ILinkableTask
 * @author chinshaw
 */
@ProxyFor(value = LinkableDashboard.class, locator = RequestFactoryEntityLocator.class)
public interface LinkableDashboardProxy extends DatastoreObjectProxy {

    /**
     * Getter for the analytics task on where to go.
     * @return
     */
	//public AnalyticsTaskProxy getAnalyticsTask();
	
	/**
	 * Getter for just the id of the analytics task that should be linked to.
	 * @return
	 */
	public Long getDashboardId();

	/**
	 * Setter for the analytics task for the linkable task. This will be the next
	 * analytics task shown when this link is selected.
	 * @param analyticsTask
	 */
	public void setDashboard(DashboardProxy dashboard);
	
	public String getContext();

	public void setContext(String context);
}
