package com.simple.orchestrator.service.jms;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.simple.orchestrator.service.hadoop.job.JobCompletionEvent;

@Singleton
public class JmsEventBridge {

	private static final Logger logger = Logger.getLogger(JmsEventBridge.class.getName());

	private static final ObjectMapper objectMaper = new ObjectMapper();

	private final EventBus eventBus;

	private final Session session;

	private Destination jobTopic;

	@Inject
	public JmsEventBridge(EventBus eventBus, Session session) {
		this.eventBus = eventBus;
		this.session = session;
	}

	public void start() throws JMSException {
		eventBus.register(this);
		jobTopic = session.createTopic("com.artisan.orchestrator.jobs");
	}

	public void stop() throws JMSException {
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
			logger.log(Level.SEVERE, "Failed to send message to topic " + jobTopic, e);
		} catch (JMSException e) {
			logger.log(Level.SEVERE, "Failed to send message to topic " + jobTopic, e);
		}
	}
}
