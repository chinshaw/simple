package com.simple.original.server.service.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import com.simple.orchestrator.api.IOperationExecutionResponse;
import com.simple.orchestrator.api.IOperationJob;
import com.simple.orchestrator.api.IOperationExecutionService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class OrchestratorService implements IOperationExecutionService {

	public static final int DEFAULT_PORT = 80;
	
	private final String host;

	private final int port;
	
	public OrchestratorService(String host) {
		this(host, DEFAULT_PORT);
	}

	public OrchestratorService(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public IOperationExecutionResponse executeSynchronous(IOperationJob operationJob) {
		Client client = Client.create();
		
		final String orchestratorUrl = "http://" + host + ":" + Integer.toString(port) + "/rest/v1";
		
		IOperationExecutionResponse response = null;
		try {
			URI basePath = new URI(orchestratorUrl);
			WebResource resource = client.resource(basePath);
			response = resource.path("operation").accept(MediaType.APPLICATION_JSON).entity(operationJob).post(IOperationExecutionResponse.class);
			
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid url " + orchestratorUrl);
		}
		
		return response;
	}
}
