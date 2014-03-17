package com.simple.engine;

import com.simple.original.api.orchestrator.IMetricKey;
import com.simple.original.api.orchestrator.ITaskExecution;


public interface ITaskExecutionDao {

	public ITaskExecution find(IMetricKey key);
	
	public ITaskExecution findLastTaskExecution(Long taskId);

	public void save(ITaskExecution execution);

}
