package com.artisan.orchestrator.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.simple.orchestrator.api.IHadoopOperationJobConfiguration;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.service.IOperationExecutionService;

public class OperationExecutionService implements IOperationExecutionService {

	public static final String SERVICE_PATH = "operation";

	private final WebTarget webTarget;

	public OperationExecutionService(Client client, String baseUrl) {
		webTarget = client.target(baseUrl + "/" + SERVICE_PATH);
	}

	/**
	 * Client Implementation
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String execute(IHadoopOperationJobConfiguration hadoopJobConfiguration) throws HadoopJobException {
		String jobReceipt = webTarget.path("execute").request(MediaType.APPLICATION_JSON).post(Entity.entity(hadoopJobConfiguration, MediaType.APPLICATION_JSON),String.class);
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
