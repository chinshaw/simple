package com.simple.orchestrator.service.web.rest;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

import com.artisan.orchestrator.rest.client.ArtisanClient;
import com.artisan.orchestrator.rest.client.BaseUrlProvider;
import com.artisan.orchestrator.rest.client.OperationExecutionService;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.simple.orchestrator.IOCApplicationInjectorFactory;
import com.simple.orchestrator.api.event.JobCompletionEvent;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.simple.orchestrator.test.OperationTestUtils;

public class TestOperationExecutionResource extends JerseyTest {



	private static final Logger logger = Logger.getLogger(TestOperationExecutionResource.class.getName());
	
	private static final ResourceConfig config = new ResourceConfig() {
		{
			register(OperationExecutionService.class);
		}
	};

	@Inject
	private EventBus eventBus;

	private ArtisanClient client;

	@Before
	public void initResource() {
		client = new ArtisanClient(client(), new BaseUrlProvider() {
			
			@Override
			public String getBaseUrl() {
				return getBaseUri().toString();
			}
		});
	}
	
	public TestOperationExecutionResource() {
		IOCApplicationInjectorFactory.getInjector().injectMembers(TestOperationExecutionResource.this);
		try {
			LogManager.getLogManager().readConfiguration(TestOperationExecutionResource.class.getResourceAsStream("/logging.properties"));
		} catch (SecurityException | IOException e) {
			throw new RuntimeException(e);
		}
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
	}

	@Override
	public Application configure() {
		return config;
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
