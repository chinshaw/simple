package com.simple.orchestrator.server;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.simple.orchestrator.api.ITaskExecutionDao;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.simple.orchestrator.server.event.EventBusConnector;
import com.simple.orchestrator.server.event.IEventConnector;
import com.simple.orchestrator.server.hadoop.ModuleProperties;
import com.simple.orchestrator.server.hadoop.mrv2.OperationDriver;
import com.simple.orchestrator.server.hbase.HBaseTaskExecutionDao;
import com.simple.orchestrator.server.service.jms.JmsEventBridge;

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