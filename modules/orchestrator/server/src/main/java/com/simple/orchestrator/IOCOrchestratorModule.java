package com.simple.orchestrator;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
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
		//install(new IOCDomainModule());

		try {
			Names.bindProperties(binder(), ModuleProperties.getInstance());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Unable to map properties, cannot continue");
		}

		bind(EventBus.class).asEagerSingleton();
		bind(IOperationExecutionService.class).to(OperationDriver.class);
		bind(ITaskExecutionDao.class).to(HBaseTaskExecutionDao.class);
		bind(JmsEventBridge.class).asEagerSingleton();
	}

	@Inject
	@Provides
	@Singleton
	public ActiveMQConnectionFactory activeMqConnectionFactory(
			@Named("com.artisan.orchestrator.queue.url") String queueUrl) {
		ActiveMQConnectionFactory mqConnectionFactory = new ActiveMQConnectionFactory(
				queueUrl);
		return mqConnectionFactory;
	}

	@Inject
	@Provides
	@Singleton
	public Connection activeMqConnection(
			ActiveMQConnectionFactory activeMqConnectionFactory)
			throws JMSException {
		Connection connection = activeMqConnectionFactory.createConnection();
		connection.start();
		return connection;
	}

	@Inject
	@Provides
	@Singleton
	public Session activeMqSession(Connection connection) throws JMSException {
		return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}
}