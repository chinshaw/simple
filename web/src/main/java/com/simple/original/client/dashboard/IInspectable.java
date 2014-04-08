package com.simple.original.client.dashboard;

import com.google.gwt.user.client.ui.IsWidget;
import com.simple.original.client.dashboard.model.IWidgetModel;

public interface IInspectable extends IsWidget {
	
	public IWidgetModel getModel();
	
	
}
