package com.artisan.orchestrator.rest.client;

import com.google.inject.AbstractModule;
import com.simple.orchestrator.api.service.IMetricService;

public class IOCClientModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IMetricService.class).to(MetricService.class);
		
	}

}
