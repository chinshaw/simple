package com.simple.original.client.dashboard;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.SkipInterfaceValidation;
import com.simple.domain.dashboard.LinkableDashboard;
import com.simple.original.client.proxy.LinkableDashboardProxy;


@SkipInterfaceValidation
public interface ILinkable {
    /**
     * Boolean to tell if the widget is linkable
     * @return
     */
    public boolean isLinkable();

	/**
	 * Returns the {@link LinkableDashboard} objects for this widget.
	 * @return
	 */
    public List<LinkableDashboardProxy> getLinkableTasks();

    /**
     * Setter for the {@link LinkableDashboard} objects of this widget.
     * @param linkableTasks
     */
    public void setLinkableTasks(List<LinkableDashboardProxy> linkableTasks);
}