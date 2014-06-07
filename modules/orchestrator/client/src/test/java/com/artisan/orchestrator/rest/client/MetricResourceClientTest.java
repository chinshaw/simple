package com.artisan.orchestrator.rest.client;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.apache.catalina.LifecycleException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simple.api.orchestrator.IMetric;
import com.simple.orchestrator.OrchestratorServer;

public class MetricResourceClientTest {

	public static final String TEST_BASE_URL = "http://localhost:52280/r/v1";

	private ArtisanClient client = ArtisanClient.create(TEST_BASE_URL);

	private static final OrchestratorServer server = OrchestratorServer.create(OrchestratorServer.DEFAULT_HOST,
			OrchestratorServer.DEFAULT_PORT, true);

	@BeforeClass
	public static void init() throws LifecycleException, InterruptedException, ServletException, IOException {
		server.start();
	}

	@AfterClass
	public static void stop() throws LifecycleException {
		server.stop();
	}

	@Test
	public void testFind() {
		MetricResourceClient service = client.createMetricService();
		IMetric metric = service.find("123:123");
		Assert.assertTrue(metric != null);
	}

	@Test
	public void testFindColumnQualifier() {

	}

	@Test
	public void testFindOperationOutputs() {
		client.enableDebug();
		MetricResourceClient service = client.createMetricService();
		List<IMetric> metrics = service.find(99999l);
		Assert.assertTrue(metrics.size() > 0);

		for (IMetric metric : metrics) {
			// System.out.println(metric.getKey());
		}

	}
}
