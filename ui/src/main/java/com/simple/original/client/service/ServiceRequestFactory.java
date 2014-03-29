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
import com.simple.original.client.proxy.JavaAnalyticsOperationProxy;
import com.simple.original.client.proxy.LinkableDashboardProxy;
import com.simple.original.client.proxy.NumberRangeProxy;
import com.simple.original.client.proxy.RAnalyticsOperationProxy;
import com.simple.original.client.proxy.UIComplexInputModelProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;
import com.simple.original.server.service.AdministrationService;
import com.simple.original.server.service.AnalyticsService;
import com.simple.original.server.service.DashboardService;
import com.simple.original.server.service.InjectingServiceLocator;
import com.simple.original.server.service.LoggingService;
import com.simple.original.server.service.SchedulerService;
import com.simple.original.shared.NotificationCriticality;

public interface ServiceRequestFactory extends RequestFactory {

	@ExtraTypes({ RAnalyticsOperationProxy.class })
	@Service(value = AnalyticsService.class, locator = InjectingServiceLocator.class)
	public interface OperationRequest extends RequestContext {

		Request<Void> executeOperation(AnalyticsOperationProxy editable);

	}

	/**
	 * Dashboard request for dealing with dashboards.
	 * 
	 * @author chris
	 */
	@ExtraTypes({ RAnalyticsOperationProxy.class, JavaAnalyticsOperationProxy.class, NumberRangeProxy.class, DashboardProxy.class,
			AnalyticsOperationInputProxy.class, UIUserInputModelProxy.class, UIComplexInputModelProxy.class, UIDateInputModelProxy.class,
			LinkableDashboardProxy.class })
	@Service(value = DashboardService.class, locator = InjectingServiceLocator.class)
	public interface DashboardRequest extends RequestContext {

		Request<DashboardProxy> getPreviousExecution(Long analyticsTaskExecutionId);

		Request<DashboardProxy> getLatestDashboard(Long analyticsTaskId);

	}

	@Service(value = AdministrationService.class)
	public interface AdministrationServiceRequest extends RequestContext {
		Request<Void> sendNotification(NotificationCriticality criticality, String notification);
	}

	@Service(value = LoggingService.class)
	public interface LoggingServiceRequest extends RequestContext {
		Request<String> getServerLog(int lineCount);

		Request<String> getTaskEngineLog(int lineCount);

		Request<String> getRLog(int lineCount);
	}


	public OperationRequest operationRequest();

	public DashboardRequest dashboardRequest();

	public LoggingServiceRequest loggingServiceRequest();

	public AdministrationServiceRequest adminiStrationServiceReqest();
}
