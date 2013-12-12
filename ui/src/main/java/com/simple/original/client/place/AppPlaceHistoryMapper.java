package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({ MonitoringPlace.Tokenizer.class,
                  AlertDefinitionEditPlace.Tokenizer.class,
                  AlertsSubscriptionPlace.Tokenizer.class,
                  AnalyticsOperationsPlace.Tokenizer.class,
                  AnalyticsTasksPlace.Tokenizer.class, 
                  AnalyticsTaskExecPlace.Tokenizer.class,
                  AnalyticsTaskSchedulerDetailsPlace.Tokenizer.class,
                  AnalyticsTaskSchedulerPlace.Tokenizer.class,
                  AnalyticsTasksSchedulerPlace.Tokenizer.class,
                  AnalyticsTaskBuilderPlace.Tokenizer.class,
                  AnalyticsTaskCopyPlace.Tokenizer.class,
                  AdministrationPlace.Tokenizer.class,
                  CopyOperationBuilderPlace.Tokenizer.class,
                  AnalyticsOperationPlace.Tokenizer.class,
                  DashboardPlace.Tokenizer.class,
                  DashboardsPlace.Tokenizer.class,
                  DashboardDesignerPlace.Tokenizer.class,
                  DataProvidersPlace.Tokenizer.class,
                  ServerLogsPlace.Tokenizer.class,
                  EmailNotificationPlace.Tokenizer.class,
                  ErrorMessagePlace.Tokenizer.class,
                  AlertsPlace.Tokenizer.class,
                  HistoricalMetricsPlace.Tokenizer.class,
                  LatestDashboardPlace.Tokenizer.class,
                  LoginPlace.Tokenizer.class,
                  MonitoringTaskPlace.Tokenizer.class,
                  PlaygroundPlace.Tokenizer.class,
                  PreferencesPlace.Tokenizer.class,
                  ReportAdminSubscriptionPlace.Tokenizer.class,
                  ReportUserSubscriptionPlace.Tokenizer.class,
                  SoftwareUpdatePlace.Tokenizer.class,
                  AlertDetailsPlace.Tokenizer.class,
                  WelcomePlace.Tokenizer.class
                  })
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
