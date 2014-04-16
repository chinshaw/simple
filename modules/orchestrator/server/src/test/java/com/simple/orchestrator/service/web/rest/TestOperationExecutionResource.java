package com.simple.orchestrator.service.web.rest;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.ws.rs.core.MediaType;

import org.apache.catalina.LifecycleException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.test.OperationTestUtils;
import com.simple.orchestrator.test.OrchestratorTestServer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestOperationExecutionResource {

	private static final Logger logger = Logger.getLogger(TestOperationExecutionResource.class.getName());
	
	static OrchestratorTestServer server = new OrchestratorTestServer();

	private WebResource resource;

	@BeforeClass
	public static void init() throws LifecycleException, InterruptedException,
			ServletException, IOException {
		server.start();
	}

	@AfterClass
	public static void stop() throws LifecycleException {
		server.stop();
	}

	@Before
	public void initResource() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(clientConfig);
		client.addFilter(new LoggingFilter(System.out));

		resource = client.resource("http://localhost:52280/r/v1/operation");
	}

	@Test
	public void testExecute() throws IOException, InterruptedException {
		logger.info("start testExecute");
		HadoopOperationJobConfiguration.Builder confBuilder = new HadoopOperationJobConfiguration.Builder();
		confBuilder.setAnalyticsOperation(OperationTestUtils
				.createTestOperation());

		CountDownLatch latch = new CountDownLatch(1);

		String jobId = resource.path("execute").type(MediaType.APPLICATION_JSON).entity(confBuilder.build())
				.post(String.class);
		
		System.out.println("jobId is " + jobId);
		assertNotNull(jobId);
		
		latch.await(10, TimeUnit.SECONDS);
		
		logger.info("jobId is " + jobId);
		assertNotNull(jobId);
		logger.info("end testExecute");
	}
}
