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
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration;
import com.simple.orchestrator.api.rest.HadoopOperationJobConfiguration.Builder;
import com.simple.orchestrator.test.OperationTestUtils;
import com.simple.orchestrator.test.OrchestratorTestServer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
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
	public void testGet() throws IOException {
		AnalyticsOperation operation = OperationTestUtils.createTestOperation();
		AnalyticsOperation savedOp = resource.entity(operation).put(AnalyticsOperation.class);
		assertNotNull(savedOp.getId());
			
		AnalyticsOperation fetched = resource.path(savedOp.getId().toString()).accept(MediaType.APPLICATION_JSON).get(AnalyticsOperation.class);
		assertNotNull(fetched);
		
		assert(fetched.getId().equals(savedOp.getId()));
		
	}
	
	@Test
	public void testGetInvalid() {
		ClientResponse response = resource.path("999999").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		assert(response.getStatus() == ClientResponse.Status.NO_CONTENT.getStatusCode());
	}
	
	@Test 
	public void testUpdate() throws IOException {
		
		// save
		RAnalyticsOperation rop = (RAnalyticsOperation) OperationTestUtils.createTestOperation();
		rop =  resource.entity(rop).put(RAnalyticsOperation.class);
		assertNotNull(rop.getId());
		
		rop.setName("NEW_NAME");
		rop.setDescription("NEW_DESCRIPTION");
		rop.setCode("NEW_CODE");
		
		// update
		rop = resource.entity(rop).post(RAnalyticsOperation.class);
		
		// Fetch again
		rop = resource.path(rop.getId().toString()).accept(MediaType.APPLICATION_JSON).get(RAnalyticsOperation.class);
		assertNotNull(rop);
		assert(rop.getName().equals("NEW_NAME"));
		assert(rop.getDescription().equals("NEW_DESCRIPTION"));
		assert(rop.getCode().equals("NEW_CODE"));
	}
	
	@Test
	public void testDelete() throws IOException {
		// save
		RAnalyticsOperation rop = (RAnalyticsOperation) OperationTestUtils.createTestOperation();
		rop =  resource.entity(rop).put(RAnalyticsOperation.class);
		assertNotNull(rop.getId());
		
		// Delete
		resource.path(rop.getId().toString()).delete();
		
		// Fetch
		ClientResponse response = resource.path(rop.getId().toString()).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		assert(response.getStatus() == ClientResponse.Status.NO_CONTENT.getStatusCode());
	}
	
	
	@Test
	public void testExecute() throws IOException {
		Builder builder   =  new HadoopOperationJobConfiguration.Builder();
		
		String value = resource.path("execute").entity(builder.build()).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class);
		assertNotNull(value);
	}
	 
	/**
	 * This test will execute a job returning the job id and then fetch the outputs
	 * from the operation when it is complete.
	 */
	@Test 
	public void testFullExecute() {
		Builder builder   =  new HadoopOperationJobConfiguration.Builder();
		String value = resource.path("execute").entity(builder.build()).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class);
		
		assertNotNull(value);
	}
}
