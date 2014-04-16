package com.simple.original.server.service.rest;

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.simple.domain.model.dataprovider.HttpDataProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestDataProviderResource {

		
	@Test
	public void testAddDataProvider() {
		
		HttpDataProvider dp = new HttpDataProvider();
		dp.setUrl("http://foo.com");
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		client.addFilter(new LoggingFilter(System.out));
		
		// Create it
		WebResource wr = client.resource("http://localhost:8080/simple/r/v1/dataprovider");
		dp = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).put(HttpDataProvider.class, dp);
		assertNotNull(dp.getId()); 
		
		// Fetch it
		ClientResponse response = null;
		response = wr.path("" + dp.getId()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		assert(response.getStatus() == 200);
		dp = response.getEntity(HttpDataProvider.class);
		assertNotNull(dp.getId());
		
		// Delete it
		response = wr.path("" +dp.getId()).type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
		assert(response.getStatus() == 200);
		
		// Check that it was deleted
		response = wr.path("" + dp.getId()).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		assert(response.getStatus() == 204);
	}
}
