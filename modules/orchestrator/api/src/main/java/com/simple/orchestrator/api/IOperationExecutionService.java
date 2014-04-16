package com.simple.orchestrator.api;

import com.simple.orchestrator.api.exception.InvalidJobIdException;
import com.simple.orchestrator.api.exception.HadoopJobException;

public interface IOperationExecutionService {
	
	/**
	 * This returns a job receipt for the job you are executing.
	 * @param operationJob
	 * @return String id of the job that is being executed. This way you can
	 * get the job status or stop the job using the job Id
	 */
	public String execute(IHadoopOperationJobConfiguration operationJob) throws HadoopJobException;
	
	/**
	 * Stop a job with it's job id.
	 * @param jobId
	 * @throws InvalidJobIdException
	 * @throws HadoopJobException 
	 */
	public void stop(String jobId) throws InvalidJobIdException, HadoopJobException;
	
	
	public IJobProgress progress(String jobId) throws InvalidJobIdException, HadoopJobException;
}
