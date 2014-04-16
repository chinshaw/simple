package com.simple.original.client.service.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.shared.NotificationCriticality;

public class NotificationEvent extends GwtEvent<NotificationEvent.Handler> {

    /**
     * Implemented by handlers of NotificationEvent.
     */
    public interface Handler extends EventHandler {
        /**
         * Called when a {@link NotificationEvent} is fired.
         * 
         * @param event
         *            the {@link NotificationEvent}
         */
        void onNotification(NotificationEvent event);
    }

    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();

    /**
     * Message to show in the panel.
     */
    private final String notificationMessage;

    private final NotificationCriticality criticality;
    
    public NotificationEvent(NotificationCriticality criticality, String notificationMessage) {
    	this.criticality = criticality;
    	this.notificationMessage = notificationMessage;
    }
    
    /**
     * Constructs a NotificationEvent for the given message.
     * 
     * @param notficationMessage
     *            a message instance
     */
    public NotificationEvent(String notificationMessage) {
        this.notificationMessage = notificationMessage;
        this.criticality = NotificationCriticality.INFO;
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
    public String getMessage() {
        return notificationMessage;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onNotification(this);
    }

	public NotificationCriticality getCriticality() {
		return criticality;
	}
}
