package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.dashboard.IDashboardWidget;

public class WidgetRemoveEvent extends GwtEvent<WidgetRemoveEvent.Handler> {

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
        void onWidgetRemove(WidgetRemoveEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();

    
    private final IDashboardWidget<?> widget;

    
    public WidgetRemoveEvent(IDashboardWidget<?> widget) {
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
    public IDashboardWidget<?> getSelectedWidget() {
        return widget;
    }
    
    @Override
    protected void dispatch(Handler handler) {
        handler.onWidgetRemove(this);
    }
}
