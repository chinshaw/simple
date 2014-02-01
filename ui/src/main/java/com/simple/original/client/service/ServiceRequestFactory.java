package com.simple.original.client.service;

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.ExtraTypes;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.AnalyticsOperationProxy;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.DashboardProxy;
import com.simple.original.client.proxy.DataProviderProxy;
import com.simple.original.client.proxy.JavaAnalyticsOperationProxy;
import com.simple.original.client.proxy.LinkableDashboardProxy;
import com.simple.original.client.proxy.MetricCollectionProxy;
import com.simple.original.client.proxy.MetricDoubleProxy;
import com.simple.original.client.proxy.MetricIntegerProxy;
import com.simple.original.client.proxy.MetricMatrixProxy;
import com.simple.original.client.proxy.MetricPlotProxy;
import com.simple.original.client.proxy.MetricStringProxy;
import com.simple.original.client.proxy.NumberRangeProxy;
import com.simple.original.client.proxy.QuartzCronTriggerProxy;
import com.simple.original.client.proxy.QuartzJobExecutionContextProxy;
import com.simple.original.client.proxy.QuartzJobKeyProxy;
import com.simple.original.client.proxy.QuartzTriggerKeyProxy;
import com.simple.original.client.proxy.QuartzTriggerProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.proxy.SqlConnectionProxy;
import com.simple.original.client.proxy.UIComplexInputModelProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;
import com.simple.original.client.proxy.ViolationProxy;
import com.simple.original.server.service.AdministrationService;
import com.simple.original.server.service.AnalyticsService;
import com.simple.original.server.service.DashboardService;
import com.simple.original.server.service.InjectingServiceLocator;
import com.simple.original.server.service.LoggingService;
import com.simple.original.server.service.SchedulerService;
import com.simple.original.server.service.SqlDriverService;
import com.simple.original.shared.NotificationCriticality;

public interface ServiceRequestFactory extends RequestFactory {

	@ExtraTypes({RAnalyticsOperationProxy.class})
	@Service(value = AnalyticsService.class, locator = InjectingServiceLocator.class)
	public interface OperationRequest extends RequestContext {

		Request<Void> executeOperation(AnalyticsOperationProxy editable);

	}

	/**
	 * Dashboard request for dealing with dashboards.
	 * 
	 * @author chris
	 */
	@ExtraTypes({ MetricCollectionProxy.class, MetricMatrixProxy.class,
			MetricIntegerProxy.class, MetricDoubleProxy.class,
			MetricStringProxy.class, MetricPlotProxy.class,
			RAnalyticsOperationProxy.class, JavaAnalyticsOperationProxy.class,
			NumberRangeProxy.class, DashboardProxy.class,
			AnalyticsOperationInputProxy.class, UIUserInputModelProxy.class,
			UIComplexInputModelProxy.class, UIDateInputModelProxy.class,
			ViolationProxy.class, LinkableDashboardProxy.class })
	@Service(value = DashboardService.class, locator = InjectingServiceLocator.class)
	public interface DashboardRequest extends RequestContext {

		Request<DashboardProxy> executeInteractive(Long taskId,
				List<AnalyticsOperationInputProxy> inputs,
				List<DataProviderProxy> dataProviders);

		Request<DashboardProxy> getPreviousExecution(
				Long analyticsTaskExecutionId);

		Request<DashboardProxy> getLatestDashboard(Long analyticsTaskId);

	}

	@Service(value = AdministrationService.class)
	public interface AdministrationServiceRequest extends RequestContext {
		Request<Void> sendNotification(NotificationCriticality criticality,
				String notification);
	}

	@Service(value = LoggingService.class)
	public interface LoggingServiceRequest extends RequestContext {
		Request<String> getServerLog(int lineCount);

		Request<String> getTaskEngineLog(int lineCount);

		Request<String> getRLog(int lineCount);
	}

	@ExtraTypes({ QuartzCronTriggerProxy.class,
			AnalyticsOperationInputProxy.class, UIDateInputModelProxy.class,
			UIUserInputModelProxy.class, UIComplexInputModelProxy.class, })
	@Service(value = SchedulerService.class)
	public interface SchedulerRequest extends RequestContext {

		Request<Void> scheduleAnalyticsTask(Long analyticsTaskId,
				List<AnalyticsOperationInputProxy> inputs,
				String cronExpression, String description, Date startDate);

		Request<List<QuartzTriggerProxy>> getScheduledJobs();

		Request<List<QuartzTriggerProxy>> searchSchedules(String searchString);

		Request<Void> unscheduleJob(String keyName, String groupName);

		Request<Void> pauseTrigger(QuartzTriggerKeyProxy triggerKey);

		Request<Void> resumeTrigger(QuartzTriggerKeyProxy triggerKey);

		Request<QuartzTriggerProxy> getTrigger(String keyName, String groupName);

		Request<Void> fireTriggerNow(QuartzTriggerKeyProxy triggerKey);

		Request<Void> fireJobNow(QuartzJobKeyProxy jobKey);

		Request<Date> rescheduleJob(QuartzTriggerKeyProxy triggerKey,
				QuartzTriggerProxy trigger);

		Request<AnalyticsTaskProxy> getTaskForTrigger(QuartzJobKeyProxy jobKey);

		Request<String> getTriggerState(QuartzTriggerKeyProxy triggerKey);

		Request<List<QuartzJobExecutionContextProxy>> getAllRunningJobs();

		Request<Boolean> cancelRunningTask(QuartzJobKeyProxy jobKey);
	}

	@Service(value = SqlDriverService.class)
	public interface SqlDataProviderRequest extends RequestContext {
		Request<Boolean> testConnection(SqlConnectionProxy driver);
	}

	public OperationRequest operationRequest();
	
	public DashboardRequest dashboardRequest();

	public SchedulerRequest schedulerRequest();

	public LoggingServiceRequest loggingServiceRequest();

	public AdministrationServiceRequest adminiStrationServiceReqest();

	public SqlDataProviderRequest sqlDataProviderRequest();
}
