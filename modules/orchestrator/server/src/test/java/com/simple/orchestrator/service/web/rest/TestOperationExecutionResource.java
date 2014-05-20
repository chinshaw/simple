package com.simple.orchestrator.service.web.rest;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.ws.rs.core.MediaType;

import org.apache.catalina.LifecycleException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.simple.orchestrator.IOCApplicationInjector;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.hadoop.job.JobCompletionEvent;
import com.simple.orchestrator.test.OperationTestUtils;
import com.simple.orchestrator.test.OrchestratorServer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestOperationExecutionResource {

	{
		try {
			LogManager.getLogManager().readConfiguration(TestOperationExecutionResource.class.getResourceAsStream("/logging.properties"));
		} catch (SecurityException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final Logger logger = Logger.getLogger(TestOperationExecutionResource.class.getName());

	static OrchestratorServer server = new OrchestratorServer();

	@Inject
	private EventBus eventBus;

	private WebResource resource;
	
	public TestOperationExecutionResource() {
		IOCApplicationInjector.getInjector().injectMembers(TestOperationExecutionResource.this);
	}

	@BeforeClass
	public static void init() throws LifecycleException, InterruptedException, ServletException, IOException {
		server.start();
	}

	@AfterClass
	public static void stop() throws LifecycleException {
		server.stop();
	}

	@Before
	public void initResource() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		client.addFilter(new LoggingFilter(System.out));

		resource = client.resource("http://localhost:52280/r/v1/operation");
	}

	@Test
	public void testExecute() throws IOException, InterruptedException {
		logger.info("start testExecute");
		HadoopOperationJobConfiguration.Builder confBuilder = new HadoopOperationJobConfiguration.Builder();
		confBuilder.setAnalyticsOperation(OperationTestUtils.createTestOperation());

		CountDownLatch latch = new CountDownLatch(1);

		String jobId = resource.path("execute").type(MediaType.APPLICATION_JSON).entity(confBuilder.build()).post(String.class);

		System.out.println("jobId is " + jobId);
		assertNotNull(jobId);

		latch.await(10, TimeUnit.SECONDS);

		logger.info("jobId is " + jobId);
		assertNotNull(jobId);
		logger.info("end testExecute");
	}

	@Test
	public void testFull() throws IOException, InterruptedException {
		logger.info("start testExecute");
		final CountDownLatch latch = new CountDownLatch(1);

		final HadoopOperationJobConfiguration.Builder confBuilder = new HadoopOperationJobConfiguration.Builder();
		confBuilder.setAnalyticsOperation(OperationTestUtils.createTestOperation());

		final String jobId = resource.path("execute").type(MediaType.APPLICATION_JSON).entity(confBuilder.build()).post(String.class);
		logger.info("submitted job => " + jobId);
		assert (jobId != null);
		logger.finest("testFull() : jobid => " + jobId);

		final Object callback = new Object() {

			@Subscribe
			public void onJobCompletion(JobCompletionEvent event) {
				if (event.getJobId().equals(jobId)) {
					// Check that our job id's match
					assert(event.getJobId().equals(jobId));
					logger.info("callback: jobid => " + event.getJobId());
					// unlock
					latch.countDown();
				}
			}
		};

		eventBus.register(callback);

		latch.await(120, TimeUnit.SECONDS);
		assert (latch.getCount() != 1);
		assertNotNull(jobId);

		logger.info("end testExecute");
	}
}
