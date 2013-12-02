package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.dashboard.model.IWidgetModel;

public class WidgetModelChangedEvent extends GwtEvent<WidgetModelChangedEvent.Handler> {

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
        void onWidgetModelChanged(WidgetModelChangedEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();

    /**
     * Message to show in the panel.
     */
    private IWidgetModel model;
    
    /**
     * Constructs a NotificationEvent for the given message.
     * 
     * @param notficationMessage
     *            a message instance
     */
    public WidgetModelChangedEvent(IWidgetModel model) {
        this.model = model;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    public IWidgetModel getWidgetModel() {
        return model;
    }
    
    @Override
    protected void dispatch(Handler handler) {
        handler.onWidgetModelChanged(this);
    }
}
