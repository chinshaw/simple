package com.simple.orchestrator;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.simple.orchestrator.api.ITaskExecutionDao;
import com.simple.orchestrator.api.rest.MetricJsonProvider;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.simple.orchestrator.hadoop.ModuleProperties;
import com.simple.orchestrator.hadoop.mrv2.OperationDriver;
import com.simple.orchestrator.hbase.HBaseTaskExecutionDao;
import com.simple.orchestrator.service.jms.JmsEventBridge;
import com.simple.orchestrator.service.web.rest.HadoopResource;
import com.simple.orchestrator.service.web.rest.HbaseResource;
import com.simple.orchestrator.service.web.rest.MessageWriter;
import com.simple.orchestrator.service.web.rest.MetricResource;
import com.simple.orchestrator.service.web.rest.Notification;
import com.simple.orchestrator.service.web.rest.OperationExecutionResource;

public class IOCOrchestratorModule extends AbstractModule {

	@Override
	protected void configure() {

		Names.bindProperties(binder(), ModuleProperties.getInstance());

		bind(EventBus.class).asEagerSingleton();
		bind(IOperationExecutionService.class).to(OperationDriver.class);
		bind(ITaskExecutionDao.class).to(HBaseTaskExecutionDao.class);
		bind(JmsEventBridge.class).asEagerSingleton();
		
		// Rest resources
		bind(MetricResource.class).in(Scopes.SINGLETON);
		bind(MessageWriter.class).in(Scopes.SINGLETON);
		bind(OperationExecutionResource.class).in(Scopes.SINGLETON);
		bind(HbaseResource.class).in(Scopes.SINGLETON);
		bind(HadoopResource.class).in(Scopes.SINGLETON);
		bind(Notification.class).in(Scopes.SINGLETON);
		
		bind(MetricJsonProvider.class).in(Scopes.SINGLETON);
	}
}