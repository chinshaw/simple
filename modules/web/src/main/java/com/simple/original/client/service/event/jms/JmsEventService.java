package com.simple.original.client.service.event.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.service.event.IEventService;
import com.simple.original.client.service.event.JobCompletionEvent;

/**
 * This service hooks onto the eventbus and will distribute events from the jms
 * queue.
 * 
 * @author chris
 * 
 */
public class JmsEventService implements IEventService, IJmsService.MessageHandler {

	public static final String CLIENT_EVENT_QUEUE = "com.artisan.web.events";

	private static final Logger logger = Logger.getLogger(JmsEventService.class.getName());

	private ConnectionCallback callback = new ConnectionCallback() {

		@Override
		public void onError(String cause) {
			GWT.log("Error " + cause);
		}

		@Override
		public void onDisconnect() {
			logger.info("JmsEventService : Disconnected");
		}

		@Override
		public void onConnect() {
			logger.info("JmsEventService: Connected");
			doRegister();
		}
	};

	private final EventBus eventBus;

	private IJmsService service;

	public JmsEventService(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void start() {
		service = new StompEventService("ws://" + Window.Location.getHostName() + ":61614/stomp", this.callback);
		service.start();
	}

	private void doRegister() {
		logger.fine("entering doRegister");
		service.addMessageHandler(CLIENT_EVENT_QUEUE, this);
	}

	@Override
	public void stop() {
		service.disconnect();
	}

	@Override
	public void pause() {
		service.disconnect();
	}

	@Override
	public void onMessage(IJmsMessage message) {
		try {
			StompHeader header = message.getStompHeader();
			String messageType = header.get("MESSAGE_TYPE");
			if (messageType != null) {
				handleEvent(messageType, message.getMessage());
				return;
			}
			logger.info("Generic JMS Message " + message.getMessage());
			eventBus.fireEvent(new JmsEvent(message));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error ", e);
		}
	}

	private void handleEvent(String messageType, String message) {
		logger.info("Message type " + messageType);

		if (messageType.equals("JOB_COMPLETION")) {
			JSONObject value = JSONParser.parseLenient(message).isObject();
			JobCompletionEvent event = new JobCompletionEvent(value.get("id").toString());

			eventBus.fireEvent(event);
			return;
		}
	}
}
