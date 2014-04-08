package com.simple.original.client.dashboard;

import com.google.gwt.user.client.ui.IsWidget;

public interface IWidgetEditor<T extends IDashboardWidget<?>> extends IsWidget {

	
	public void setDashboardWidget(T widget);
	
	public T getDashboardWidget();
	
	
}