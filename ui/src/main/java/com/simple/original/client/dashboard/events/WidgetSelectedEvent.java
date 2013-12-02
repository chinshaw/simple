package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.dashboard.AbstractDashboardWidget;

public class WidgetSelectedEvent extends GwtEvent<WidgetSelectedEvent.Handler> {

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
        void onWidgetSelected(WidgetSelectedEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();

    /**
     * Message to show in the panel.
     */
    private final AbstractDashboardWidget<?> widget;
    
    /**
     * Constructs a NotificationEvent for the given message.
     * 
     * @param notficationMessage
     *            a message instance
     */
    public WidgetSelectedEvent(AbstractDashboardWidget<?> widget) {
        this.widget = widget;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Return the new message.
     * 
     * @return a message
     */
    public AbstractDashboardWidget<?> getSelectedWidget() {
        return this.widget;
    }
    
    @Override
    protected void dispatch(Handler handler) {
        handler.onWidgetSelected(this);
    }
}
