package com.simple.original.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.simple.domain.AnalyticsOperationInput;
import com.simple.domain.AnalyticsTask;
import com.simple.domain.AnalyticsTaskExecution;
import com.simple.domain.DataProvider;
import com.simple.domain.dashboard.Dashboard;
import com.simple.domain.dashboard.DashboardDao;
import com.simple.domain.dashboard.DashboardUtils;
import com.simple.domain.metric.Metric;
import com.simple.engine.service.AnalyticsService;
import com.simple.engine.service.AnalyticsTaskExecutionException;
import com.simple.original.api.exceptions.DashboardException;

public class DashboardService {

	private static final Logger logger = Logger.getLogger(DashboardService.class.getName());
	
	private final DashboardDao dashboardDao;
	
	private final AnalyticsService analyticsService;

	@Inject
	public DashboardService(DashboardDao dashboardDao, AnalyticsService analyticsService) {
		this.dashboardDao = dashboardDao;
		this.analyticsService = analyticsService;
	}
	
	public Dashboard executeInteractive(Long dashboardId,
			List<AnalyticsOperationInput> inputs,
			List<DataProvider> dataProviders) throws DashboardException {
		
		Dashboard dashboard = dashboardDao.find(dashboardId);
		if (dashboard == null) {
			throw new DashboardException("Inavlid dashboard id " + dashboardId);
		}
		
		return executeInteractive(dashboard, inputs, dataProviders);
	}
	
	public Dashboard executeInteractive(Dashboard dashboard,
			List<AnalyticsOperationInput> inputs,
			List<DataProvider> dataProviders) throws DashboardException {
		
		if (dashboard == null) {
			throw new DashboardException("Invalid dashboard specified");
		}
		
		AnalyticsTask task = dashboard.getAnalyticsTask();
		try {
			AnalyticsTaskExecution execution = analyticsService.executeAnalyticsTask(task, inputs, dataProviders);
			Map<Long, Metric> outputsMap = createMetricMap(execution.getExecutionMetrics());
			DashboardUtils.bindDashboard(dashboard, outputsMap);	
		} catch (AnalyticsTaskExecutionException e) {
			throw new DashboardException("Unable to execute task", e);
		}
		
		return dashboard;
	}

	public Dashboard getPreviousExecution(Long dashboardId) {
		Dashboard dashboard = dashboardDao.findDashboardForTask(dashboardId);
		
		//Map<Long, Metric> outputsMap = createMetricMap(dashboard.
		//		.getExecutionMetrics());
		//DashboardUtils.bindDashboard(dashboard, outputsMap);
		return null;

	}

	public Dashboard getLatestDashboard(Long dashboardId) {
	
		return null;
	}
	
	private Dashboard fetchDashboard(Long taskId, List<Metric> outputs)
			throws DashboardException {
		if (taskId == null) {
			throw new IllegalArgumentException(
					"You must not be null when retrieving a dashboard");
		}

		logger.info("fetchDashboard outputs size is " + outputs);

		// Set the dashboard here because merge, does not return the same
		// entity. It returns a new attached copy of the entity.
		Dashboard dashboard = dashboardDao.findDashboardForTask(taskId);

		Map<Long, Metric> outputsMap = null;
		try {
			outputsMap = createMetricMap(outputs);
			// outputsMap = MetricUtils.mericListAsMap(outputs);
		} catch (IllegalArgumentException ill) {
			// one of our metrics is null or has a null name.
			logger.log(Level.SEVERE,
					"Unable to convert metrics to map because ", ill);
			for (Metric output : outputs) {
				if (output != null) {
					logger.info("known metric -> " + output.toString());
				}
			}
		}

		if (dashboard != null) {
			DashboardUtils.bindDashboard(dashboard, outputsMap);
		}

		return dashboard;
	}
	
	/**
	 * Creates a map of the metrics with the key of the
	 * AnalyticsOperationOutptuts#getId()
	 * 
	 * This can be used in generating the dashboard so that the widget can
	 * figure out which metric it cares about when binding the dashboard.
	 * 
	 * TODO this needs to be moved somewhere else. probably another utils class.
	 * 
	 * @param metrics
	 * @return
	 */
	public static Map<Long, Metric> createMetricMap(List<Metric> metrics) {
		if (metrics.size() < 1) {
			logger.fine("There were no metrics found in the outputs");
		}
		HashMap<Long, Metric> metricMap = new HashMap<Long, Metric>();
		for (Metric metric : metrics) {
			logger.info("Adding metric with origin id "
					+ metric.getOrigin().getId());
			metricMap.put(metric.getOrigin().getId(), metric);
		}
		return metricMap;
	}
	
	


}
