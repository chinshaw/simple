package com.simple.original.client.service.event.jms;

import java.net.MalformedURLException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.service.event.IEventService;
import com.simple.original.client.service.event.JmsEvent;

/**
 * This service hooks onto the eventbus and will distribute events from the jms
 * queue.
 * 
 * @author chris
 * 
 */
public class JmsEventService implements IEventService, IJmsService.MessageHandler {

	private final EventBus eventBus;

	private final IJmsService service;

	public JmsEventService(EventBus eventBus) {
		this(eventBus, new StompEventService());
	}

	public JmsEventService(EventBus eventBus, IJmsService service) {
		this.eventBus = eventBus;
		this.service = service;

	}

	public void start() throws MalformedURLException {
		service.setUrl("ws://" + GWT.getHostPageBaseURL() + ":61614/stomp");
		Window.alert("url is " + service.getUrl());
		GWT.log("connecting to url " + service.getUrl());
		service.setConnectionCallback(new ConnectionCallback() {

			@Override
			public void onError(StompMessage cause) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDisconnect() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onConnect() {
				// TODO Auto-generated method stub

			}
		});
		service.addMessageHandler(this);
	}

	@Override
	public void stop() {
		throw new RuntimeException("TODO");
	}

	@Override
	public void pause() {
		throw new RuntimeException("TODO");
	}

	@Override
	public void onMessage(IJmsMessage message) {
		eventBus.fireEvent(new JmsEvent(message));
	}
}
