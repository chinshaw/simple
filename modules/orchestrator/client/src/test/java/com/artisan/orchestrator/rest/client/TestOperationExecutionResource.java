package com.artisan.orchestrator.rest.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.simple.orchestrator.IOCApplicationInjector;
import com.simple.orchestrator.api.event.JobCompletionEvent;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.simple.orchestrator.service.web.WebContextInjectorListener;
import com.simple.orchestrator.test.OperationTestUtils;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class TestOperationExecutionResource extends JerseyTest {

	public static final String TEST_BASE_URL = "http://localhost:52280/r/v1";

	{
		try {
			LogManager.getLogManager().readConfiguration(TestOperationExecutionResource.class.getResourceAsStream("/logging.properties"));
		} catch (SecurityException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final Logger logger = Logger.getLogger(TestOperationExecutionResource.class.getName());


	@Inject
	private EventBus eventBus;

	private ArtisanClient client;
	
	public TestOperationExecutionResource() {
		IOCApplicationInjector.getInjector().injectMembers(TestOperationExecutionResource.this);
	}

    protected AppDescriptor configure() {
    	return new WebAppDescriptor.Builder().contextListenerClass(WebContextInjectorListener.class).build();
    }

	@Before
	public void initResource() {
		client = ArtisanClient.create(TEST_BASE_URL);
		client.enableDebug();
	}

	@Test
	public void testExecute() throws IOException, InterruptedException, HadoopJobException {
		logger.info("start testExecute");
		HadoopOperationJobConfiguration.Builder confBuilder = new HadoopOperationJobConfiguration.Builder();
		confBuilder.setAnalyticsOperation(OperationTestUtils.createTestOperation());

		CountDownLatch latch = new CountDownLatch(1);

		IOperationExecutionService service = client.createExecutionService();

		String jobId = service.execute(confBuilder.build());
		assertNotNull(jobId);

		latch.await(10, TimeUnit.SECONDS);
		assertNotNull(jobId);
		logger.info("end testExecute");
	}

	@Test
	public void testFull() throws IOException, InterruptedException, HadoopJobException {
		logger.info("start testExecute");
		final CountDownLatch latch = new CountDownLatch(1);

		final HadoopOperationJobConfiguration.Builder confBuilder = new HadoopOperationJobConfiguration.Builder();
		confBuilder.setAnalyticsOperation(OperationTestUtils.createTestOperation());

		IOperationExecutionService opExec = client.createExecutionService();

		final String jobId = opExec.execute(confBuilder.build());

		// final String jobId =
		// resource.path("execute").type(MediaType.APPLICATION_JSON).entity(confBuilder.build()).post(String.class);
		logger.info("submitted job => " + jobId);
		assert (jobId != null);
		logger.finest("testFull() : jobid => " + jobId);

		final Object callback = new Object() {

			@Subscribe
			public void onJobCompletion(JobCompletionEvent event) {
				if (event.getJobId().equals(jobId)) {
					// Check that our job id's match
					assert (event.getJobId().equals(jobId));
					logger.info("callback: jobid => " + event.getJobId());
					// unlock
					latch.countDown();
				}
			}
		};

		eventBus.register(callback);
		
		// Start max wait if we get to two minutes, for readability I use TimeUnit
		latch.await(120, TimeUnit.SECONDS);

		assert (latch.getCount() != 1);
		assertNotNull(jobId);

		logger.info("end testExecute");
	}
}
