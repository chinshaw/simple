package com.artisan.orchestrator.rest.client;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.sun.jersey.api.client.WebResource;

public class OperationResourceClient implements IOperationExecutionService {

	public static final String SERVICE_PATH = "operation";

	private final WebResource resource;

	public OperationResourceClient(ArtisanClient client, String baseUrl) {
		resource = client.resource(UriBuilder.fromPath(baseUrl)
				.path(IOperationExecutionService.RESOURCE_PATH)
				.build().toString());
	}

	/**
	 * Client Implementation
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String execute(IHadoopJobConfiguration hadoopJobConfiguration) throws HadoopJobException {
		String jobReceipt = resource.path("execute").type(MediaType.APPLICATION_JSON).entity(hadoopJobConfiguration).post(String.class);
		return jobReceipt;
	}

	@Override
	public void stop(String jobId) throws InvalidJobIdException, HadoopJobException {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public IJobProgress progress(String jobId) throws InvalidJobIdException, HadoopJobException {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

}
