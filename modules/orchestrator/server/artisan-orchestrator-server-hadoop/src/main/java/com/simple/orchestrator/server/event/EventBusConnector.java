package com.simple.orchestrator.server.event;

import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;

@Singleton
public class EventBusConnector implements IEventConnector {

	private final EventBus eventBus = new EventBus();
	
	@Override
	public void subscribe(Object object) {
		eventBus.register(object);
	}

	@Override
	public void post(Object event) {
		eventBus.post(event);
	}
}
