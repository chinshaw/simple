package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.dashboard.IDashboardWidget;

/**
 * This event is fired after a widget is added to any dashboard panel, it is
 * useful when dropping a widget on the panel.
 * 
 * @author chinshaw
 */
public class WidgetAddedEvent extends GwtEvent<WidgetAddedEvent.Handler> {

	public interface Handler extends EventHandler {

		void onWidgetAdd(WidgetAddedEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final IDashboardWidget<?> widget;

	public WidgetAddedEvent(IDashboardWidget<?> widget) {
		this.widget = widget;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public IDashboardWidget<?> getCreatedWidget() {
		return widget;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onWidgetAdd(this);
	}
}