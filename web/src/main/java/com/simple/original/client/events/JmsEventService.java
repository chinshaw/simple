package com.simple.original.client.events;

import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.service.jms.IJmsMessage;
import com.simple.original.client.service.jms.IJmsService;

/**
 * This service hooks onto the eventbus and will distribute events
 * from the jms queue.
 * @author chris
 *
 */
public class JmsEventService {

	
	
	private final IJmsService.MessageHandler jmsMessageHandler = new IJmsService.MessageHandler() {
		
		@Override
		public void onMessage(IJmsMessage messge) {
			eventBus.fireEvent(new JmsMessage);
		}
	};
	
	
	private final EventBus eventBus;
	
	private final IJmsService jmsService;
	
	public JmsEventService(EventBus eventBus, IJmsService jmsService) {
		this.eventBus = eventBus;
		this.jmsService = jmsService;
	}
	
	public void start() {
		jmsService.addMessageHandler(jmsMessageHandler);
	}
	
	
	
	
}
