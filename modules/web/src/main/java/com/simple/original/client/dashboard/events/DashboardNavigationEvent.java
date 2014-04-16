package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DashboardNavigationEvent extends GwtEvent<DashboardNavigationEvent.Handler> {

    /**
     * Implemented by handlers of NotificationEvent.
     */
    public interface Handler extends EventHandler {
        /**
         * Called when a {@link LinkableWidgetSelectedEvent} is fired.
         * 
         * @param event
         *            the {@link LinkableWidgetSelectedEvent}
         */
        void onDashboardNavigationEvent(DashboardNavigationEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();

    /**
     * Message to show in the panel.
     */
    private Long dashboardId;
    
    /**
     * Constructs a NotificationEvent for the given message.
     * 
     * @param notficationMessage
     *            a message instance
     */
    public DashboardNavigationEvent(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    public Long getDashboardId() {
        return dashboardId;
    }
    
    @Override
    protected void dispatch(Handler handler) {
        handler.onDashboardNavigationEvent(this);
    }
}
