package com.artisan.orchestrator.hadoop.job;

import com.artisan.orchestrator.hadoop.job.event.EventBusConnector;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.simple.orchestrator.api.event.IEventConnector;

public class IOCJobModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(IEventConnector.class).to(EventBusConnector.class).in(Singleton.class);
	}

}
