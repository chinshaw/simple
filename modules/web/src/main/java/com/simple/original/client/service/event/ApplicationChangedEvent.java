package com.simple.original.client.service.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.place.shared.PlaceChangeEvent;

public class ApplicationChangedEvent extends GwtEvent<ApplicationChangedEvent.Handler> {

    
    /**
     * Implemented by handlers of PlaceChangeEvent.
     */
    public interface Handler extends EventHandler {
      /**
       * Called when a {@link PlaceChangeEvent} is fired.
       *
       * @param event the {@link PlaceChangeEvent}
       */
      void onApplicationChange(ApplicationChangedEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();
    
    private String applicationName = "";
    
    public ApplicationChangedEvent(String applicationName) {
        this.applicationName = applicationName;
    }
    
    @Override
    protected void dispatch(Handler handler) {
      handler.onApplicationChange(this);
    }
    
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
        return TYPE;
    }
}
