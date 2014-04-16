package com.simple.original.client.service.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.place.shared.PlaceChangeEvent;

public class ApplicationSignOutEvent extends GwtEvent<ApplicationSignOutEvent.Handler> {

    
    /**
     * Implemented by handlers of PlaceChangeEvent.
     */
    public interface Handler extends EventHandler {
      /**
       * Called when a {@link PlaceChangeEvent} is fired.
       *
       * @param event the {@link PlaceChangeEvent}
       */
      void onSignOut(ApplicationSignOutEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();
    
    @Override
    protected void dispatch(Handler handler) {
      handler.onSignOut(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
        return TYPE;
    }
}
