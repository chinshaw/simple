package com.simple.engine;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.simple.domain.IOCDomainModule;
import com.simple.engine.hbase.HBaseTaskExecutionDao;
import com.simple.engine.service.AnalyticsTaskService;
import com.simple.engine.service.IAnalyticsOperationExecutor;
import com.simple.engine.service.hadoop.ModuleProperties;
import com.simple.engine.service.hadoop.mrv2.OperationDriver;

public class IOCEngineModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCDomainModule());
		
		try {
			Names.bindProperties(binder(), ModuleProperties.getInstance());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to map properties, cannot continue");
		}
		
		bind(IAnalyticsOperationExecutor.class).to(OperationDriver.class);
		bind(ITaskExecutionDao.class).to(HBaseTaskExecutionDao.class);
		bind(AnalyticsTaskService.class).in(Singleton.class);
	}
}
