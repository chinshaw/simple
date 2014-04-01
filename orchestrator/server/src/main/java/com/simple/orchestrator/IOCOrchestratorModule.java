package com.simple.orchestrator;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.simple.domain.IOCDomainModule;
import com.simple.orchestrator.api.IOperationExecutionService;
import com.simple.orchestrator.api.ITaskExecutionDao;
import com.simple.orchestrator.hbase.HBaseTaskExecutionDao;
import com.simple.orchestrator.service.hadoop.ModuleProperties;
import com.simple.orchestrator.service.hadoop.mrv2.OperationDriver;

public class IOCOrchestratorModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCDomainModule());

		try {
			Names.bindProperties(binder(), ModuleProperties.getInstance());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to map properties, cannot continue");
		}

		bind(IOperationExecutionService.class).to(OperationDriver.class);
		bind(ITaskExecutionDao.class).to(HBaseTaskExecutionDao.class);
	}
}