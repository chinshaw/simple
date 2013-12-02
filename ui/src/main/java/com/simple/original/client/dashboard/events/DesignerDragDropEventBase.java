package com.simple.original.client.dashboard.events;

import com.google.gwt.event.dom.client.DragDropEventBase;
import com.google.gwt.event.shared.EventHandler;
import com.simple.original.client.dashboard.AbstractDashboardWidget;

public abstract class DesignerDragDropEventBase<H extends EventHandler> extends DragDropEventBase<H> {

	private AbstractDashboardWidget<?> dashboardWidget;

	public AbstractDashboardWidget<?> getDashboardWidget() {
		return dashboardWidget;
	}

	public void setDashboardWidget(AbstractDashboardWidget<?> dashboardWidget) {
		this.dashboardWidget = dashboardWidget;
	}
}