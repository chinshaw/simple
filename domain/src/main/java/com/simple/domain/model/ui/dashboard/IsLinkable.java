package com.simple.domain.model.ui.dashboard;

import java.util.List;

public interface IsLinkable {
    /**
     * Boolean to tell if the widget is linkable
     * @return
     */
	public boolean isLinkable();

	/**
	 * Returns the {@link LinkableDashboard} objects for this widget.
	 * @return
	 */
    public List<LinkableDashboard> getLinkableTasks();
    
    /**
     * Setter for the {@link LinkableDashboard} objects of this widget.
     * @param linkableTasks
     */
    public void setLinkableTasks(List<LinkableDashboard> linkableTasks);
}
