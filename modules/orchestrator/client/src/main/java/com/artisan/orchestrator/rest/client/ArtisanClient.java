package com.artisan.orchestrator.rest.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandler;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * This class is merely a wrapper around the jersey library but it gives us a
 * defined api that we can extend and expose what we want. Rather than exposing
 * everything that jersey supports and confusing the user this makes an easy api
 * that can be understandable.
 * 
 * @author chris
 */
public class ArtisanClient implements ClientHandler {

	private final Client client;

	private final String baseUrl;

	private ArtisanClient(ClientConfig config, String baseUrl) {
		this.client = Client.create(config);
		this.baseUrl = baseUrl;
	}

	public void enableDebug() {
		client.addFilter(new LoggingFilter(System.out));
	}

	public OperationExecutionService createExecutionService() {
		return new OperationExecutionService(this, baseUrl);
	}

	@Override
	public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
		return client.handle(cr);
	}

	/**
	 * Used to create a client that can communicate with the server instance.
	 * 
	 * @param clientConfig
	 * @param baseUrl
	 * @return
	 */
	public static final ArtisanClient create(String baseUrl) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		return new ArtisanClient(clientConfig, baseUrl);
	}

	protected WebResource resource(String urlString) {
		return client.resource(urlString);
	}

}