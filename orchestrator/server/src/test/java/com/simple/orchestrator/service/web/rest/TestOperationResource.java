package com.simple.orchestrator.service.web.rest;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.ws.rs.core.MediaType;

import org.apache.catalina.LifecycleException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.orchestrator.api.rest.OperationJob;
import com.simple.orchestrator.test.OperationTestUtils;
import com.simple.orchestrator.test.OrchestratorTestServer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestOperationResource {
	
	static OrchestratorTestServer orchestrator = new OrchestratorTestServer();
	
	private WebResource resource;
	
	
	@BeforeClass
	public static void init() throws LifecycleException, InterruptedException, ServletException, IOException {
		orchestrator.start();		
	}
	
	@AfterClass
	public static void stop() throws LifecycleException {
		orchestrator.stop();
	}
	
	@Before
	public void initResource() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		client.addFilter(new LoggingFilter(System.out));
		
		resource = client.resource("http://localhost:52280/r/v1/operation");
	}
	
	
	public TestOperationResource() throws IOException {

	}

	@Test
	public void testPut() throws IOException {
		AnalyticsOperation operation = OperationTestUtils.createTestOperation();
		AnalyticsOperation savedOp = resource.entity(operation).put(AnalyticsOperation.class);
		assertNotNull(savedOp.getId());
	}
	
	@Test
	public void testGet() {
		
	}
	
	@Test
	public void testGetInvalid() {
		
	}
	
	@Test 
	public void testUpdate() {
		
	}
	
	@Test
	public void testDelete() {
		
	}
	
	
	@Test
	public void testExecute() throws IOException {
		OperationJob job = new OperationJob();
		String value =resource.path("execute").entity(job).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class);
		
		assertNotNull(value);
		
	}
	 
}
