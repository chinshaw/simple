package com.simple.orchestrator.api.service;

import com.simple.orchestrator.api.IJobProgress;
import com.simple.orchestrator.api.conf.IHadoopJobConfiguration;
import com.simple.orchestrator.api.exception.HadoopJobException;
import com.simple.orchestrator.api.exception.InvalidJobIdException;

public interface IOperationExecutionService {
	
	public static final String RESOURCE_PATH = "operation";
	
	/**
	 * This returns a job receipt for the job you are executing.
	 * @param operationJob
	 * @return String id of the job that is being executed. This way you can
	 * get the job status or stop the job using the job Id
	 */
	public String execute(IHadoopJobConfiguration operationJob) throws HadoopJobException;
	
	/**
	 * Stop a job with it's job id.
	 * @param jobId
	 * @throws InvalidJobIdException
	 * @throws HadoopJobException 
	 */
	public void stop(String jobId) throws InvalidJobIdException, HadoopJobException;
	
	
	public IJobProgress progress(String jobId) throws InvalidJobIdException, HadoopJobException;
}
