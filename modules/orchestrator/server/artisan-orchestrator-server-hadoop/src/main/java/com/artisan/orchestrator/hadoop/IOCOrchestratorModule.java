package com.artisan.orchestrator.hadoop;

import com.artisan.orchestrator.hadoop.hadoop.mrv2.OperationDriver;
import com.artisan.orchestrator.hadoop.hbase.HBaseTaskExecutionDao;
import com.artisan.orchestrator.hadoop.job.event.EventBusConnector;
import com.artisan.orchestrator.hadoop.service.jms.JmsEventBridge;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.simple.orchestrator.api.ITaskExecutionDao;
import com.simple.orchestrator.api.event.IEventConnector;
import com.simple.orchestrator.api.service.IOperationExecutionService;

public class IOCOrchestratorModule extends AbstractModule {

	@Override
	protected void configure() {

		Names.bindProperties(binder(), ModuleProperties.getInstance());

		bind(EventBus.class).asEagerSingleton();
		bind(IEventConnector.class).to(EventBusConnector.class).in(Singleton.class);
		bind(IOperationExecutionService.class).to(OperationDriver.class);
		bind(ITaskExecutionDao.class).to(HBaseTaskExecutionDao.class);
		bind(JmsEventBridge.class).asEagerSingleton();
	}
}