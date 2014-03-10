package com.simple.original.client.service;

import java.util.List;

import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.api.analytics.ITaskExecution;

public interface IAnalyticsTaskService {

	ITaskExecution executeAnalyticsTask(AnalyticsTask task, List<AnalyticsOperationInput> inputs, List<DataProvider> dataProviders);

}
