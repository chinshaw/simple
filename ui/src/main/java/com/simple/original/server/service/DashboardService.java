package com.simple.original.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.simple.api.exceptions.DashboardException;
import com.simple.api.orchestrator.IMetric;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.dashboard.Dashboard;
import com.simple.domain.model.ui.dashboard.DashboardDao;
import com.simple.domain.model.ui.dashboard.DashboardUtils;
import com.simple.original.client.service.IAnalyticsTaskService;

public class DashboardService {

	private static final Logger logger = Logger.getLogger(DashboardService.class.getName());

	private final DashboardDao dashboardDao;

	private final IAnalyticsTaskService analyticsService;

	@Inject
	public DashboardService(DashboardDao dashboardDao, IAnalyticsTaskService analyticsService) {
		this.dashboardDao = dashboardDao;
		this.analyticsService = analyticsService;
	}

	public Dashboard executeInteractive(Long dashboardId, List<AnalyticsOperationInput> inputs, List<DataProvider> dataProviders)
			throws DashboardException {

		Dashboard dashboard = dashboardDao.find(dashboardId);
		if (dashboard == null) {
			throw new DashboardException("Inavlid dashboard id " + dashboardId);
		}

		return executeInteractive(dashboard, inputs, dataProviders);
	}

	public Dashboard executeInteractive(Dashboard dashboard, List<AnalyticsOperationInput> inputs, List<DataProvider> dataProviders)
			throws DashboardException {
		return null;
		//
//		if (dashboard == null) {
//			throw new DashboardException("Invalid dashboard specified");
//		}
//
//		AnalyticsTask task = dashboard.getAnalyticsTask();
//
//		ITaskExecution execution = analyticsService.executeAnalyticsTask(task, inputs, dataProviders);
//		// Map<Long, Metric> outputsMap =
//		// createMetricMap(execution.getExecutionMetrics());
//		// DashboardUtils.bindDashboard(dashboard, outputsMap);
//
//		return dashboard;
		
	}

	public Dashboard getPreviousExecution(Long dashboardId) {
		Dashboard dashboard = dashboardDao.findDashboardForTask(dashboardId);

		// Map<Long, Metric> outputsMap = createMetricMap(dashboard.
		// .getExecutionMetrics());
		// DashboardUtils.bindDashboard(dashboard, outputsMap);
		return null;

	}

	public Dashboard getLatestDashboard(Long dashboardId) {

		return null;
	}

	private Dashboard fetchDashboard(Long taskId, List<IMetric> outputs) throws DashboardException {
		if (taskId == null) {
			throw new IllegalArgumentException("You must not be null when retrieving a dashboard");
		}

		logger.info("fetchDashboard outputs size is " + outputs);

		// Set the dashboard here because merge, does not return the same
		// entity. It returns a new attached copy of the entity.
		Dashboard dashboard = dashboardDao.findDashboardForTask(taskId);

		Map<Long, IMetric> outputsMap = null;
		try {
			outputsMap = createMetricMap(outputs);
			// outputsMap = MetricUtils.mericListAsMap(outputs);
		} catch (IllegalArgumentException ill) {
			// one of our metrics is null or has a null name.
			logger.log(Level.SEVERE, "Unable to convert metrics to map because ", ill);
			for (IMetric output : outputs) {
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
	public static Map<Long, IMetric> createMetricMap(List<IMetric> metrics) {
		if (metrics.size() < 1) {
			logger.fine("There were no metrics found in the outputs");
		}
		HashMap<Long, IMetric> metricMap = new HashMap<Long, IMetric>();
		for (IMetric metric : metrics) {

			// metricMap.put(metric.getOrigin().getId(), metric);
		}
		return metricMap;
	}

}
