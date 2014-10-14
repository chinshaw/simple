package com.simple.orchestrator.server.service.jms;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.simple.orchestrator.api.event.JobCompletionEvent;

@Singleton
public class JmsEventBridge {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(JmsEventBridge.class
			.getName());

	/**
	 * Object mapper used to serialized events.
	 */
	private static final ObjectMapper objectMaper = new ObjectMapper();

	private final ActiveMQConnectionFactory activeMqConnectionFactory;
	
	private final EventBus eventBus;

	private Destination jobTopic;
	
	private Connection connection = null;
	
	private Session session = null;
	


	@Inject
	public JmsEventBridge(EventBus eventBus, @Named("com.artisan.orchestrator.queue.url") String queueUrl) {
		activeMqConnectionFactory = new ActiveMQConnectionFactory(queueUrl);
		this.eventBus = eventBus;

	}

	public void start() throws JMSException {
		logger.fine("start()");
		
		connection = activeMqConnectionFactory.createConnection();
		this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		jobTopic = session.createTopic("com.artisan.orchestrator.jobs");
		connection.start();
		
		eventBus.register(this);
	}

	public void stop() throws JMSException {
		connection.stop();
		session.close();
		eventBus.unregister(this);
	}

	@Subscribe
	public void onJobCompletionEvent(JobCompletionEvent event) {
		logger.fine("Job completed " + event.getJobId());
		// Create a MessageProducer from the Session to the Topic or Queue
		MessageProducer producer = null;
		try {
			try {
				producer = session.createProducer(jobTopic);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

				String json = objectMaper.writeValueAsString(event);
				TextMessage textMessage = session.createTextMessage(json);
				producer.send(textMessage);
			} finally {
				if (producer != null) {
					producer.close();
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to send message to topic "
					+ jobTopic, e);
		} catch (JMSException e) {
			logger.log(Level.SEVERE, "Failed to send message to topic "
					+ jobTopic, e);
		}
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}
	
	public Session getJmsSession() {
		return session;
	}
}
