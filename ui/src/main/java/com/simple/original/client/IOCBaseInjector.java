package com.simple.original.client;

import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.web.bindery.event.shared.EventBus;
import com.simple.original.client.activity.AdministrationActivity;
import com.simple.original.client.activity.AlertDefinitionActivity;
import com.simple.original.client.activity.AlertDefinitionEditActivity;
import com.simple.original.client.activity.AnalyticsOperationBuilderActivity;
import com.simple.original.client.activity.AnalyticsOperationsActivity;
import com.simple.original.client.activity.AnalyticsTaskBuilderActivity;
import com.simple.original.client.activity.AnalyticsTaskExecActivity;
import com.simple.original.client.activity.AnalyticsTaskSchedulerActivity;
import com.simple.original.client.activity.AnalyticsTaskSubscriptionActivity;
import com.simple.original.client.activity.AnalyticsTasksActivity;
import com.simple.original.client.activity.AnalyticsTasksSchedulerActivity;
import com.simple.original.client.activity.DashboardsActivity;
import com.simple.original.client.activity.DataProvidersActivity;
import com.simple.original.client.activity.EmailNotificationActivity;
import com.simple.original.client.activity.LoginActivity;
import com.simple.original.client.activity.MonitoringTaskLimitActivity;
import com.simple.original.client.activity.PreferencesActivity;
import com.simple.original.client.activity.ServerLogsActivity;
import com.simple.original.client.activity.SubscriptionActivity;
import com.simple.original.client.activity.TopPanelActivity;
import com.simple.original.client.activity.WelcomeActivity;
import com.simple.original.client.dashboard.activity.DashboardActivity;
import com.simple.original.client.dashboard.activity.DashboardDesignerActivity;
import com.simple.original.client.dashboard.designer.IDashboardDesignerView;
import com.simple.original.client.place.ContentActivityMapper;
import com.simple.original.client.place.PlaceController;
import com.simple.original.client.place.TopActivityMapper;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.service.DaoRequestFactory;
import com.simple.original.client.service.PublicRequestFactory;
import com.simple.original.client.service.ServiceRequestFactory;
import com.simple.original.client.view.IAdministrationView;
import com.simple.original.client.view.IAlertDefinitionEditView;
import com.simple.original.client.view.IAlertDefinitionView;
import com.simple.original.client.view.IAlertDetailsView;
import com.simple.original.client.view.IAnalyticsTaskDesignerView;
import com.simple.original.client.view.IAnalyticsTaskExecutionView;
import com.simple.original.client.view.IAnalyticsTaskSchedulerDetailsView;
import com.simple.original.client.view.IAnalyticsTaskSchedulerView;
import com.simple.original.client.view.IAnalyticsTaskSubscriptionView;
import com.simple.original.client.view.IAnalyticsTaskView;
import com.simple.original.client.view.IAnalyticsTasksSchedulerView;
import com.simple.original.client.view.IDashboardView;
import com.simple.original.client.view.IDataProvidersView;
import com.simple.original.client.view.IEmailNotificationView;
import com.simple.original.client.view.IFactoryAlertsView;
import com.simple.original.client.view.ILoginView;
import com.simple.original.client.view.IMasterLayoutPanel;
import com.simple.original.client.view.IMonitoringTaskLimitView;
import com.simple.original.client.view.IOperationBuilderView;
import com.simple.original.client.view.IOperationsView;
import com.simple.original.client.view.IPreferencesView;
import com.simple.original.client.view.IServerLogsView;
import com.simple.original.client.view.ISubscriptionView;
import com.simple.original.client.view.ITopPanelView;
import com.simple.original.client.view.IWelcomeView;
import com.simple.original.client.view.widgets.LoggerPanel;
import com.simple.original.client.view.widgets.NotificationPanel;

public interface IOCBaseInjector extends Ginjector {

	EventBus eventBus();

	PlaceController placeController();

	ServiceRequestFactory serviceRequestFactory();

	DaoRequestFactory daoRequestFactory();

	PublicRequestFactory publicRequestFactory();

	Resources resources();

	PlaceHistoryMapper placeHistoryMapper();
	
	ContentActivityMapper contentActivityMapper();
	
	TopActivityMapper topActivityMapper();
	
	//
	// Actrivities
	//

	AdministrationActivity administrationActivity();

	AlertDefinitionActivity alertDefinitionActivity();

	AlertDefinitionEditActivity alertDefinitionEditActivity();

	AnalyticsOperationBuilderActivity analyticsOperationBuilderActivity();

	AnalyticsOperationsActivity analyticsOperationsActivity();

	AnalyticsTaskBuilderActivity analyticsTaskBuilderActivity();

	AnalyticsTaskExecActivity analyticsTaskExecActivity();

	AnalyticsTasksActivity analyticsTasksActivity();

	AnalyticsTaskSchedulerActivity analyticsTaskSchedulerActivity();

	AnalyticsTasksSchedulerActivity analyticsTasksSchedulerActivity();

	DataProvidersActivity dataProvidersActivity();
	
	
	/**
	 * Used to view a single dashboard, this is the rendering of the dashboard.
	 * @return
	 */
	DashboardActivity dashboardActivity();
	
	/**
	 * Activity to list and edit and search all the dashboards.
	 * @return
	 */
	DashboardsActivity dashboardsActivity();
	
	/**
	 * Activity to design a dashboard, drag and drop designer.
	 * @return
	 */
	DashboardDesignerActivity dashboardDesignerActivity();

	
	EmailNotificationActivity emailNotificationActivity();

	LoginActivity loginActivity();

	MonitoringTaskLimitActivity monitoringTaskLimitActivity();

	PreferencesActivity preferencesActivity();

	AnalyticsTaskSubscriptionActivity reportSubscriptionsActivity();

	ServerLogsActivity serverLogsActivity();

	SubscriptionActivity subscriptionActivity();

	TopPanelActivity topPanelActivity();

	WelcomeActivity welcomeActivity();

	// //////
	// Views
	// //////

	ILoginView getLoginView();

	IAnalyticsTaskView getAnalyticsTasksView();

	IAnalyticsTaskView getReportTasksView();

	IAnalyticsTaskDesignerView getAnalyticsTaskEditView();

	IAnalyticsTaskExecutionView getAnalyticsTaskExecView();

	// ReportTaskExecView getReportTaskExecView();
	

	
	IMasterLayoutPanel getMasterLayoutPanel();

	ITopPanelView getTopPanelView();

	IFactoryAlertsView getFactoryAlertsView();

	IPreferencesView getPreferencesView();

	IAnalyticsTaskSchedulerDetailsView getAnalyticsTaskSchedulerDetailsView();

	IAnalyticsTaskSchedulerView getAnalyticsTaskSchedulerView();

	IMonitoringTaskLimitView getMonitoringTaskLimitView();

	IOperationsView operationsView();

	IAnalyticsTasksSchedulerView getAnalyticsTasksSchedulerView();

	IOperationBuilderView operationBuilderView();
	
	ISubscriptionView getSubscriptionView();

	IAlertDefinitionView getAlertDefinitionView();

	IAlertDefinitionEditView getAlertDefinitionEditView();

	IEmailNotificationView getEmailNotificationView();

	NotificationPanel getNotificationPanel();

	IDashboardDesignerView getDashboardDesignerView();

	LoggerPanel getLoggerPanel();

	IAnalyticsTaskSubscriptionView getReportSubscriptionView();

	IAdministrationView getApplicationHealthDisplay();

	IDataProvidersView getDataProvidersView();

	IServerLogsView getDebuggingView();

	IAlertDetailsView getAlertDetailsView();

	IDashboardView getDashboardView();

	IWelcomeView getWelcomeView();

	//
	// Helpers
	//

	void injectMembers(Simple simple);

}
