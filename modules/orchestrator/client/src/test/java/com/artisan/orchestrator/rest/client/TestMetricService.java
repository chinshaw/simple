package com.artisan.orchestrator.rest.client;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.simple.api.orchestrator.IMetric;

public class TestMetricService {

	public static final String TEST_BASE_URL = "http://localhost:52280/r/v1";

	private ArtisanClient client = null; // ArtisanClient.create(TEST_BASE_URL);

	@Test
	public void testFind() {
		MetricService service = client.createMetricService();
		IMetric metric = service.find("123:123");
		Assert.assertTrue(metric != null);
	}

	@Test
	public void testFindColumnQualifier() {

	}

	@Test
	public void testFindOperationOutputs() {
		MetricService service = client.createMetricService();
		List<IMetric> metrics = service.find(99999l);
		Assert.assertTrue(metrics.size() > 0);

		for (IMetric metric : metrics) {
			// System.out.println(metric.getKey());
		}
	}

	@Test
	public void system() {
		System.out.println(System.getProperty("java.library.path"));
	}
}
