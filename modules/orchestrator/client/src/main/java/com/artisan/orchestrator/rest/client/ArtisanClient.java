package com.artisan.orchestrator.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;


/**
 * This class is merely a wrapper around the jersey library but it gives us a
 * defined api that we can extend and expose what we want. Rather than exposing
 * everything that jersey supports and confusing the user this makes an easy api
 * that can be understandable.
 * 
 * @author chris
 */
public class ArtisanClient {
	
	private static final String BASE_URL_TEMPLATE = "%s://%s:%s/rest/v1";

	private final Client client;

	private final BaseUrlProvider baseUrlProvider;
	
	public ArtisanClient(final String protocol, final String host, final String port) {
		this(ClientBuilder.newClient(), new BaseUrlProvider() {
			
			@Override
			public String getBaseUrl() {
				return String.format(BASE_URL_TEMPLATE, protocol, host, port);
			}
		});
	}
	
	
	public ArtisanClient(Client client, BaseUrlProvider baseUrlProvider) {
		this.client = client;
		this.baseUrlProvider = baseUrlProvider;
	}

	public OperationExecutionService createExecutionService() {
		return new OperationExecutionService(client, baseUrlProvider.getBaseUrl());
	}
	
	public MetricService createMetricService() {
		return new MetricService(client, baseUrlProvider.getBaseUrl());
	}
}