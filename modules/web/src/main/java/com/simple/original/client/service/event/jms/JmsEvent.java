package com.simple.original.client.service.event.jms;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class JmsEvent extends GwtEvent<JmsEvent.Handler> {

	public interface Handler extends EventHandler {

		void onJmsEvent(JmsEvent event);

	}

	/**
	 * A singleton instance of Type&lt;Handler&gt;.
	 */
	public static final Type<JmsEvent.Handler> TYPE = new Type<JmsEvent.Handler>();

	private final IJmsMessage message;
	
	/**
	 * Construct a new event with the jms message, this should
	 * always be in a Javascript type object.
	 * 
	 * @param message
	 */
	public JmsEvent(IJmsMessage message) {
		this.message = message;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onJmsEvent(this);
	}

	public IJmsMessage getMessage() {
		return message;
	}
}