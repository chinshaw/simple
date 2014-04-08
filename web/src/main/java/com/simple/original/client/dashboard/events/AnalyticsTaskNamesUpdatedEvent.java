package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Simple event used to alert all the widgets that care about the analytics task names
 * that the list of names has been updated. 
 * @author chinshaw
 */
public class AnalyticsTaskNamesUpdatedEvent extends GwtEvent<AnalyticsTaskNamesUpdatedEvent.Handler> {

    public interface Handler extends EventHandler {
       
        
        void onAnalyticsTaskNamesUpdated(AnalyticsTaskNamesUpdatedEvent event);
    }

    public static final Type<Handler> TYPE = new Type<Handler>();

    public AnalyticsTaskNamesUpdatedEvent() {
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onAnalyticsTaskNamesUpdated(this);
    }
}