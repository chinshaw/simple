package com.simple.orchestrator.hadoop.mrv2;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.artisan.orchestrator.hadoop.hadoop.mrv2.OperationDriver;
import com.artisan.orchestrator.hadoop.job.mapper.NullOperationMapper;
import com.artisan.orchestrator.hadoop.job.reducer.NullOperationReducer;
import com.google.common.eventbus.Subscribe;
import com.simple.orchestrator.OrchestratorTest;
import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.event.IEventConnector;
import com.simple.orchestrator.api.event.OperationReducerStateChange;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration.Builder;

public class TestOperationDriverEventing extends OrchestratorTest {

	public static Logger logger = Logger.getLogger(TestOperationDriverEventing.class.getName());

	private static final CountDownLatch latch = new CountDownLatch(1);

	@Inject
	private OperationDriver executor;

	@Inject
	private IEventConnector eventConnector;

	@Before
	public void before() {
		getInjector().injectMembers(this);
	}

	/**
	 * This needs to test that the driver will send a basic operation started
	 * event. If it does this will unlock the latch and complete the task and
	 * move on.
	 * @throws IOException 
	 * @throws HadoopJobException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testStartedEvent() throws IOException, HadoopJobException, InterruptedException {
		logger.info("testGraphic");

		Builder builder = new HadoopOperationJobConfiguration.Builder();

		NullOperationMapper mapper = new NullOperationMapper();
		NullOperationReducer reducer = new NullOperationReducer();
		
		IHadoopJobConfiguration conf = null; //builder.setMapperOperation(mapper).setReducer(reducer).build();
		
		executor.execute(conf);
		
		logger.info("Connector is in test" + eventConnector);
		eventConnector.subscribe(new Object() {
			@Subscribe
			public void onReducerStateChange(OperationReducerStateChange event) {
				System.out.println("GOTTTT THEE EVENT FOR " + event.getState().name());
			}
		});

		executor.execute(conf);
		latch.await(300, TimeUnit.SECONDS);
	}
}
