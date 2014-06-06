package com.artisan.orchestrator.rest.client;

import com.sun.jersey.api.client.Client;
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
public class ArtisanClient {

	public static final int CLIENT_DEFAULT_PORT = 80;
	
	public static final String CLIENT_DEFAULT_PROTOCOL = "http";
	
	private final Client client;

	private final String baseUrl;

	
	public ArtisanClient(String host) {
		this(host, CLIENT_DEFAULT_PORT);
	}
	
	public ArtisanClient(String host, int port) {
		this(host, port, CLIENT_DEFAULT_PROTOCOL);
	}
	
	public ArtisanClient(String host, int port, String protocol) {
		this(new Client(), String.format("%s://%s:%s",  protocol, host, port));
	}
	
	public ArtisanClient(Client client, String baseUrl) {
		this.client = client;
		this.baseUrl = baseUrl;
	}
	
	
	private ArtisanClient(ClientConfig config, String baseUrl) {
		this.client = Client.create(config);
		this.baseUrl = baseUrl;
	}

	/**
	 * This will print all messages to stdout for debugging
	 * usage.
	 */
	public void enableDebug() {
		client.addFilter(new LoggingFilter(System.out));
	}

	public OperationExecutionService createExecutionService() {
		return new OperationExecutionService(this, baseUrl);
	}
	
	public MetricService createMetricService() {
		return new MetricService(this, baseUrl);
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