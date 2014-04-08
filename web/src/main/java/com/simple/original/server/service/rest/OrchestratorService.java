package com.simple.original.server.service.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.IOperationExecutionResponse;
import com.simple.orchestrator.api.IHadoopOperationJobConfiguration;
import com.simple.orchestrator.api.IOperationExecutionService;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.exception.HadoopJobException;
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
	public String execute(IHadoopOperationJobConfiguration operationJob)
			throws HadoopJobException {
		Client client = Client.create();

		final String orchestratorUrl = "http://" + host + ":"
				+ Integer.toString(port) + "/rest/v1";

		IOperationExecutionResponse response = null;
		try {
			URI basePath = new URI(orchestratorUrl);
			WebResource resource = client.resource(basePath);
			response = resource.path("operation")
					.accept(MediaType.APPLICATION_JSON).entity(operationJob)
					.post(IOperationExecutionResponse.class);

		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid url " + orchestratorUrl);
		}

		return "FOO";
	}

	@Override
	public void stop(String jobId) throws InvalidJobIdException {
		// TODO Auto-generated method stub

	}

	@Override
	public IJobProgress progress(String jobId) throws InvalidJobIdException {
		// TODO Auto-generated method stub
		return null;
	}
}
