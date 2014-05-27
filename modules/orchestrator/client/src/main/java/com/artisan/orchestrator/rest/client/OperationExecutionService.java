package com.artisan.orchestrator.rest.client;

import javax.ws.rs.core.MediaType;

import com.simple.orchestrator.api.IHadoopOperationJobConfiguration;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.IOperationExecutionService;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.sun.jersey.api.client.WebResource;

public class OperationExecutionService implements IOperationExecutionService {

	public static final String SERVICE_PATH = "operation";

	private final WebResource resource;

	public OperationExecutionService(ArtisanClient client, String baseUrl) {
		resource = client.resource(baseUrl + "/operation");
	}

	/**
	 * Client Implementation
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String execute(IHadoopOperationJobConfiguration hadoopJobConfiguration) throws HadoopJobException {
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
