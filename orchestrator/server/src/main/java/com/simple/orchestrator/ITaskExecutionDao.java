package com.simple.orchestrator;

import com.simple.api.orchestrator.IMetricKey;
import com.simple.api.orchestrator.ITaskExecution;


public interface ITaskExecutionDao {

	public ITaskExecution find(IMetricKey key);
	
	public ITaskExecution findLastTaskExecution(Long taskId);

	public void save(ITaskExecution execution);

}
