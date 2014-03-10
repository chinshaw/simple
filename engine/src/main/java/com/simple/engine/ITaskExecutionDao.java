package com.simple.engine;

import com.simple.engine.api.IMetricKey;
import com.simple.original.api.analytics.ITaskExecution;


public interface ITaskExecutionDao {

	public ITaskExecution find(IMetricKey key);
	
	public ITaskExecution findLastTaskExecution(Long taskId);

	public void save(ITaskExecution execution);

}
