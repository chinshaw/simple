package com.simple.original.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.simple.original.client.activity.AdministrationActivity;
import com.simple.original.client.activity.AnalyticsOperationBuilderActivity;
import com.simple.original.client.activity.AnalyticsOperationsActivity;
import com.simple.original.client.activity.AnalyticsTaskBuilderActivity;
import com.simple.original.client.activity.AnalyticsTaskExecActivity;
import com.simple.original.client.activity.AnalyticsTaskSchedulerActivity;
import com.simple.original.client.activity.AnalyticsTasksActivity;
import com.simple.original.client.activity.AnalyticsTasksSchedulerActivity;
import com.simple.original.client.activity.DashboardsActivity;
import com.simple.original.client.activity.DataProvidersActivity;
import com.simple.original.client.activity.LoginActivity;
import com.simple.original.client.activity.PreferencesActivity;
import com.simple.original.client.activity.ServerLogsActivity;
import com.simple.original.client.activity.TopPanelActivity;
import com.simple.original.client.activity.WelcomeActivity;
import com.simple.original.client.dashboard.activity.DashboardActivity;
import com.simple.original.client.dashboard.activity.DashboardDesignerActivity;
import com.simple.original.client.place.AppPlaceHistoryMapper;
import com.simple.original.client.place.ContentActivityMapper;
import com.simple.original.client.place.PlaceController;
import com.simple.original.client.place.TopActivityMapper;
import com.simple.original.client.resources.Resources;
import com.simple.original.client.view.IAdministrationView;
import com.simple.original.client.view.IAnalyticsTaskDesignerView;
import com.simple.original.client.view.IAnalyticsTaskExecutionView;
import com.simple.original.client.view.IAnalyticsTaskSchedulerDetailsView;
import com.simple.original.client.view.IAnalyticsTaskSchedulerView;
import com.simple.original.client.view.IAnalyticsTaskView;
import com.simple.original.client.view.IAnalyticsTasksSchedulerView;
import com.simple.original.client.view.IDashboardsView;
import com.simple.original.client.view.IDataProvidersView;
import com.simple.original.client.view.ILoginView;
import com.simple.original.client.view.IMasterLayoutPanel;
import com.simple.original.client.view.IOperationBuilderView;
import com.simple.original.client.view.IOperationsView;
import com.simple.original.client.view.IPreferencesView;
import com.simple.original.client.view.IServerLogsView;
import com.simple.original.client.view.ITopPanelView;
import com.simple.original.client.view.IWelcomeView;
import com.simple.original.client.view.desktop.AnalyticsOperationBuilderView;
import com.simple.original.client.view.desktop.AnalyticsOperationsView;
import com.simple.original.client.view.desktop.AnalyticsTaskDesignerView;
import com.simple.original.client.view.desktop.AnalyticsTaskExecView;
import com.simple.original.client.view.desktop.AnalyticsTaskSchedulerDetailsView;
import com.simple.original.client.view.desktop.AnalyticsTaskSchedulerView;
import com.simple.original.client.view.desktop.AnalyticsTasksSchedulerView;
import com.simple.original.client.view.desktop.AnalyticsTasksView;
import com.simple.original.client.view.desktop.ApplicationAdministrationView;
import com.simple.original.client.view.desktop.DashboardsView;
import com.simple.original.client.view.desktop.DataProvidersView;
import com.simple.original.client.view.desktop.LoginView;
import com.simple.original.client.view.desktop.MasterLayoutPanel;
import com.simple.original.client.view.desktop.PreferencesView;
import com.simple.original.client.view.desktop.ServerLogsView;
import com.simple.original.client.view.desktop.TopPanelView;
import com.simple.original.client.view.desktop.WelcomeView;
import com.simple.original.client.view.widgets.LoggerPanel;
import com.simple.original.client.view.widgets.NotificationPanel;

public class IOCDesktopModule extends AbstractGinModule {

	@Override
	protected void configure() {

		// bind(DesktopInjector.class).toProvider(DesktopInjectorProvider.class);
		bind(TopActivityMapper.class).in(Singleton.class);
		bind(ContentActivityMapper.class).in(Singleton.class);
		bind(Application.class).in(Singleton.class);

		// Activities
		// bind(AbstractActivity.class);
		// bind(AbstractTaskBuilderActivity.class);
		bind(AdministrationActivity.class);
		bind(AnalyticsOperationBuilderActivity.class);
		bind(AnalyticsOperationsActivity.class);
		bind(AnalyticsTaskBuilderActivity.class);
		bind(AnalyticsTaskExecActivity.class);
		bind(AnalyticsTasksActivity.class);
		bind(AnalyticsTaskSchedulerActivity.class);
		bind(AnalyticsTasksSchedulerActivity.class);
		bind(DashboardActivity.class);
		bind(DashboardsActivity.class);
		bind(DashboardDesignerActivity.class);
		bind(DataProvidersActivity.class);
		bind(LoginActivity.class);
		bind(PreferencesActivity.class);
		bind(ServerLogsActivity.class);
		bind(TopPanelActivity.class);
		bind(WelcomeActivity.class);

		// Views
		bind(IAnalyticsTaskExecutionView.class).to(AnalyticsTaskExecView.class).in(Singleton.class);
		bind(IAnalyticsTaskDesignerView.class).to(AnalyticsTaskDesignerView.class).in(Singleton.class);
		bind(IAnalyticsTaskSchedulerView.class).to(AnalyticsTaskSchedulerView.class).in(Singleton.class);
		bind(IAnalyticsTaskView.class).to(AnalyticsTasksView.class).in(Singleton.class);

		bind(IAnalyticsTaskSchedulerDetailsView.class).to(AnalyticsTaskSchedulerDetailsView.class).in(Singleton.class);
		bind(IOperationsView.class).to(AnalyticsOperationsView.class).in(Singleton.class);

		bind(IOperationBuilderView.class).to(AnalyticsOperationBuilderView.class).in(Singleton.class);
		bind(IAnalyticsTasksSchedulerView.class).to(AnalyticsTasksSchedulerView.class).in(Singleton.class);

		bind(IAdministrationView.class).to(ApplicationAdministrationView.class).in(Singleton.class);
		bind(IDataProvidersView.class).to(DataProvidersView.class).in(Singleton.class);
	
		bind(LoggerPanel.class).in(Singleton.class);
		bind(NotificationPanel.class).in(Singleton.class);

	
		bind(IDashboardsView.class).to(DashboardsView.class).in(Singleton.class);
	
		bind(ILoginView.class).to(LoginView.class).in(Singleton.class);
		bind(IMasterLayoutPanel.class).to(MasterLayoutPanel.class).in(Singleton.class);
		bind(IPreferencesView.class).to(PreferencesView.class).in(Singleton.class);
		bind(IServerLogsView.class).to(ServerLogsView.class).in(Singleton.class);
		bind(ITopPanelView.class).to(TopPanelView.class);
		bind(IWelcomeView.class).to(WelcomeView.class).in(Singleton.class);

		bind(LoginActivity.class);
		bind(WelcomeActivity.class);

	}

	@Provides
	@Singleton
	EventBus getEventBus() {
		return new ResettableEventBus(new SimpleEventBus());
	}
	
	@Provides
	@Singleton
	Resources resources() {
		return Resources.INSTANCE;
	}

	@Provides
	@Singleton
	PlaceHistoryMapper placeHistoryMapper() {
		return GWT.create(AppPlaceHistoryMapper.class);
	}

	@Provides
	@Inject
	@Singleton
	PlaceController placeController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}


}
