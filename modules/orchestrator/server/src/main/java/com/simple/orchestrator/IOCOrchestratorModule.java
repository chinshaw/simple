package com.simple.orchestrator;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.simple.orchestrator.api.IOperationExecutionService;
import com.simple.orchestrator.api.ITaskExecutionDao;
import com.simple.orchestrator.hadoop.ModuleProperties;
import com.simple.orchestrator.hadoop.mrv2.OperationDriver;
import com.simple.orchestrator.hbase.HBaseTaskExecutionDao;
import com.simple.orchestrator.service.jms.JmsEventBridge;

public class IOCOrchestratorModule extends AbstractModule {

	@Override
	protected void configure() {

		Names.bindProperties(binder(), ModuleProperties.getInstance());

		bind(EventBus.class).asEagerSingleton();
		bind(IOperationExecutionService.class).to(OperationDriver.class);
		bind(ITaskExecutionDao.class).to(HBaseTaskExecutionDao.class);
		bind(JmsEventBridge.class).asEagerSingleton();
	}
}