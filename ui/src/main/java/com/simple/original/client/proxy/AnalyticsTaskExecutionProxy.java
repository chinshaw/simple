package com.simple.original.client.proxy;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.model.AnalyticsTaskExecution;
import com.simple.original.api.analytics.IAnalyticsTaskExecution.TaskCompletionStatus;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = AnalyticsTaskExecution.class, locator = RequestFactoryEntityLocator.class)
public interface AnalyticsTaskExecutionProxy extends DatastoreObjectProxy {
    
    public String[] EXECUTION_PROPERTIES = {
    		"analyticsTaskInputs",
    		"executionMetrics",
    		"executionMetrics.value",
    		"executionMetrics.value.value",
    		
    		"dashboard", 
            "dashboard.widgets",
            "dashboard.widgets.linkableTasks",
            "dashboard.widgets.linkableTasks.analyticsTask.id",
            "dashboard.widgets.metric",
            "dashboard.widgets.metric.id",
            "dashboard.widgets.metric.value",
            "dashboard.widgets.ranges",
            "dashboard.widgets.metric.violations",
            "dashboard.widgets.metric.columns",
            "dashboard.widgets.metric.rows",
            "dashboard.widgets.metric.headers",
            "dashboard.widgets.metric.columns.value.value.value",
            
            "dashboard.widgets.widgets",
            "dashboard.widgets.widgets.linkableTasks",
            "dashboard.widgets.widgets.linkableTasks.analyticsTask.id",
            "dashboard.widgets.widgets.metric",
            "dashboard.widgets.widgets.metric.id",
            "dashboard.widgets.widgets.metric.value",
            "dashboard.widgets.widgets.ranges",
            "dashboard.widgets.widgets.metric.rows",
            "dashboard.widgets.widgets.metric.headers",
            "dashboard.widgets.widgets.metric.violations"
    };

    public String[] HISTORY_PROPERTIES = { "completionStatus", "analyticsTask" };

    public Date getStartTime();

    public Date getCompletionTime();

    public AnalyticsTaskProxy getAnalyticsTask();
    
    public Long getAnalyticsTaskId();

    public TaskCompletionStatus getCompletionStatus();
    
    public List<AnalyticsOperationInputProxy> getAnalyticsTaskInputs();
    
    public List<MetricProxy> getExecutionMetrics();
    
    public String getExecutionLogFileName();
}
