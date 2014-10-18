package com.simple.orchestrator.service.jms;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.broker.BrokerService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.artisan.orchestrator.hadoop.service.jms.JmsEventBridge;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.simple.orchestrator.IOCOrchestratorTestModule;
import com.simple.orchestrator.api.event.JobCompletionEvent;

public class TestJmsEventBridge {

	static {
		try {
			LogManager.getLogManager().readConfiguration(JmsEventBridge.class.getResourceAsStream("/logging.properties"));
		} catch (SecurityException | IOException e) {
			throw new RuntimeException("Unable to initialize properties");
		}
	}
	
	
	@Inject
	private JmsEventBridge eventBridge;
	
	private static final ObjectMapper jacksonMapper = new ObjectMapper();

	private static BrokerService broker = new BrokerService();

	@BeforeClass
	public static void init() throws Exception {
		broker.setPersistent(false);
		broker.start();
	}

	@AfterClass
	public static void close() throws Exception {
		broker.stop();
	}

	public TestJmsEventBridge() {
		Injector injector = Guice.createInjector(new IOCOrchestratorTestModule());
		injector.injectMembers(this);
	}
	
	/**
	 * This will test that the job completion event can be fied and then 
	 * put to the correct jms queue.  It sets up a listener on the
	 * jms event topic and listens for the event.
	 * 
	 * 
	 * @throws InterruptedException
	 * @throws JMSException
	 */
	@Test
	public void testJobCompletion() throws InterruptedException, JMSException {
		final String JOB_NAME = "TEST_JOB_COMPLETION";
		final String JOB_STATUS = "SUCCESS";
		final CountDownLatch latch = new CountDownLatch(1);
		final Session session = eventBridge.getJmsSession();
		final EventBus eventBus = eventBridge.getEventBus();
		
		// Start the event jms bridge
		eventBridge.start();
		
		// Create a topic and listen for jobs
		Topic topic = session.createTopic("com.artisan.orchestrator.jobs");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				try {
					if (message instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) message;
						JobCompletionEvent completionEvent = jacksonMapper.readValue(textMessage.getText(), JobCompletionEvent.class);
						assert(completionEvent != null);
						assert(completionEvent.getJobId().equals(JOB_NAME));
						assert(completionEvent.getJobStatus().equals(JOB_STATUS));
					}
				} catch (JMSException | IOException e) {
					System.out.println("Caught:" + e);
					e.printStackTrace();
				}

				latch.countDown();
			}
		});


		eventBus.register(this);
		eventBus.post(new JobCompletionEvent(JOB_NAME, JOB_STATUS));

		latch.await(130, TimeUnit.SECONDS);
		session.close();
		assert (latch.getCount() != 1);
	}
}