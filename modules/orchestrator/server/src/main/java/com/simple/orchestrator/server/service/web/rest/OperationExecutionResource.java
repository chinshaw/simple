package com.simple.orchestrator.server.service.web.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.orchestrator.api.IHadoopOperationJobConfiguration;
import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.rest.MediaType;
import com.simple.orchestrator.api.service.IOperationExecutionService;
import com.simple.orchestrator.server.hadoop.mrv2.OperationDriver;

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
	public String execute(IHadoopOperationJobConfiguration operationJob)
			throws HadoopJobException {
		List<AnalyticsOperationInput> inputs = (List<AnalyticsOperationInput>) operationJob
				.getUserInputs();
		List<DataProvider> dataProviders = (List<DataProvider>) operationJob
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
