package com.simple.original.server.service.rest;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class IOCRestModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(DataProviderResource.class).in(Scopes.SINGLETON);
		serve("/r/v1/*").with(GuiceContainer.class, createJerseyParams());
	}
	
	private Map<String, String> createJerseyParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		//params.put("com.sun.jersey.spi.container.ResourceFilters",
		//		"com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFacto‌​ry");

		return params;
	}
}
