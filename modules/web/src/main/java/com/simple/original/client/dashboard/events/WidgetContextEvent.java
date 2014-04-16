package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.dashboard.AbstractDashboardWidget;

public class WidgetContextEvent extends GwtEvent<WidgetContextEvent.Handler> {

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
        void onWidgetContext(WidgetContextEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();
    
    private final AbstractDashboardWidget<?> widget;
    
    //private final ContextMenuEvent contextEvent;
    
    private final int clientX;
    private final int clientY;
    
    public WidgetContextEvent(AbstractDashboardWidget<?> widget, int clientX, int clientY) {
        this.widget = widget;
        //this.contextEvent = contextEvent;
        this.clientX = clientX;
        this.clientY = clientY;
    }

    public int getClientX() {
		return clientX;
	}

	public int getClientY() {
		return clientY;
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
    public AbstractDashboardWidget<?>  getSelectedWidget() {
        return widget;
    }
    
    @Override
    protected void dispatch(Handler handler) {
        handler.onWidgetContext(this);
    }
}
