package com.simple.engine;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.simple.engine.rest.HadoopResource;
import com.simple.engine.rest.HbaseResource;
import com.simple.engine.rest.MessageWriter;
import com.simple.engine.rest.MetricResource;
import com.simple.engine.rest.OperationResource;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class IOCEngineRestModule extends ServletModule {

	public void configureServlets() {
		install(new IOCEngineModule());
		
		bind(MetricResource.class).in(Scopes.SINGLETON);
		bind(MessageWriter.class).in(Scopes.SINGLETON);
		bind(OperationResource.class).in(Scopes.SINGLETON);
		bind(HbaseResource.class).in(Scopes.SINGLETON);
		bind(HadoopResource.class).in(Scopes.SINGLETON);

		serve("/rest/v1/*").with(GuiceContainer.class);
	}
}
