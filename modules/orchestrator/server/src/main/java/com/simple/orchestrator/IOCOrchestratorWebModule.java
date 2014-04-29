package com.simple.orchestrator;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.simple.orchestrator.service.web.rest.HadoopResource;
import com.simple.orchestrator.service.web.rest.HbaseResource;
import com.simple.orchestrator.service.web.rest.MessageWriter;
import com.simple.orchestrator.service.web.rest.MetricResource;
import com.simple.orchestrator.service.web.rest.Notification;
import com.simple.orchestrator.service.web.rest.OperationExecutionResource;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class IOCOrchestratorWebModule extends ServletModule {

	@Override
	public void configureServlets() {
		install(new IOCOrchestratorModule());

		bind(MetricResource.class).in(Scopes.SINGLETON);
		bind(MessageWriter.class).in(Scopes.SINGLETON);
		bind(OperationExecutionResource.class).in(Scopes.SINGLETON);
		bind(HbaseResource.class).in(Scopes.SINGLETON);
		bind(HadoopResource.class).in(Scopes.SINGLETON);
		bind(Notification.class).in(Scopes.SINGLETON);
		serve("/r/v1/*").with(GuiceContainer.class, createJerseyParams());
	}

	private Map<String, String> createJerseyParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		return params;
	}
}