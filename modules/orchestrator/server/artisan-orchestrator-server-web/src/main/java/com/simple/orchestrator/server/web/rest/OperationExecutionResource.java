package com.simple.orchestrator.server.web.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.artisan.orchestrator.hadoop.hadoop.mrv2.OperationDriver;
import com.google.inject.Inject;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.dataprovider.IDataProvider;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.hadoop.operation.IOperationInput;
import com.simple.orchestrator.api.rest.MediaType;
import com.simple.orchestrator.api.service.IOperationExecutionService;

@Path("/operation")
public class OperationExecutionResource implements IOperationExecutionService {

	private final IOperationExecutionService driver;


	@Inject
	public OperationExecutionResource(OperationDriver driver) {
		this.driver = driver;
	}
	
	@POST
	@Path("/execute")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_PROTOBUF })
	public String execute(IHadoopJobConfiguration operationJob)
			throws HadoopJobException {
		List<IOperationInput> inputs = (List<IOperationInput>) operationJob
				.getUserInputs();
		List<IDataProvider> dataProviders = (List<IDataProvider>) operationJob
				.getDataProviders();
		return driver.execute(operationJob);
	}

	@Override
	public void stop(String jobId) throws InvalidJobIdException, HadoopJobException {
		driver.stop(jobId);
	}

	@Override
	public IJobProgress progress(String jobId) throws InvalidJobIdException {
		return null;
		
	}
}
