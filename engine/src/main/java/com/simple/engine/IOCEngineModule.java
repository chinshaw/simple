package com.simple.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.simple.domain.IOCDomainModule;
import com.simple.engine.service.IAnalyticsOperationExecutor;
import com.simple.engine.service.AnalyticsService;
import com.simple.engine.service.R.RServeService;

public class IOCEngineModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCDomainModule());
		Names.bindProperties(binder(), getEngineProperties());
		
		bind(IAnalyticsOperationExecutor.class).to(RServeService.class);
		bind(AnalyticsService.class).in(Singleton.class);
	}
	

	Properties getEngineProperties() {
		Properties props = new Properties();
		try {
			InputStream stream = IOCEngineModule.class.getClassLoader().getResourceAsStream("engine.properties");
			props.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
}
