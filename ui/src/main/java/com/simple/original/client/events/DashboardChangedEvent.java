package com.simple.original.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.simple.original.client.proxy.DashboardProxy;

public class DashboardChangedEvent extends
		GwtEvent<DashboardChangedEvent.Handler> {

	/**
	 * Implemented by handlers of PlaceChangeEvent.
	 */
	public interface Handler extends EventHandler {
		/**
		 * Called when a {@link PlaceChangeEvent} is fired.
		 * 
		 * @param event
		 *            the {@link PlaceChangeEvent}
		 */
		void onDashboardChanged(DashboardChangedEvent event);
	}

	/**
	 * A singleton instance of Type&lt;Handler&gt;.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();

	private DashboardProxy dashboard;

	public DashboardChangedEvent(DashboardProxy dashboard) {
		this.dashboard = dashboard;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onDashboardChanged(this);
	}

	public DashboardProxy getDashboard() {
		return dashboard;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
}
