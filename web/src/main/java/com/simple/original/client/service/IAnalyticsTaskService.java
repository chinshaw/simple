package com.simple.original.client.service;

import java.util.List;

import com.simple.api.orchestrator.ITaskExecution;
import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;

public interface IAnalyticsTaskService {

	ITaskExecution executeAnalyticsTask(AnalyticsTask task, List<AnalyticsOperationInput> inputs, List<DataProvider> dataProviders);

}
