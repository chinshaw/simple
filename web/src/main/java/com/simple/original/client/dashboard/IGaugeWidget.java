package com.simple.original.client.dashboard;

import com.google.gwt.user.client.ui.IsWidget;
import com.simple.original.client.dashboard.events.WidgetModelChangedEvent;
import com.simple.original.client.dashboard.model.IGaugeWidgetModel;
import com.simple.original.client.view.widgets.LinkableWidget;

public interface IGaugeWidget extends IDashboardWidget<IGaugeWidgetModel>, LinkableWidget, IInspectable, WidgetModelChangedEvent.Handler, IsWidget {

	public Number getValue();
	
	public void setValue(Number value);
}
