package com.simple.original.client.dashboard.activity;

import com.google.inject.Inject;
import com.simple.original.client.place.HistoricalMetricsPlace;
import com.simple.original.client.proxy.AnalyticsTaskExecutionProxy;
import com.simple.original.client.service.ServiceRequestFactory.DashboardRequest;
import com.simple.original.client.view.IDashboardView;

public class HistoricalDashboardActivity extends DashboardActivity {

	@Inject
    public HistoricalDashboardActivity(IDashboardView view) {
        super(view);
    }

    @Override
    protected DashboardRequest getDashboardUpdateRequest() {
        HistoricalMetricsPlace historyPlace = (HistoricalMetricsPlace) place();
        DashboardRequest analyticsRequest = service().dashboardRequest();
        if (historyPlace.getAnalyticsExecutionId() == null) {
            display.showError("Analytics task execution id is not specified in the path, check the url or use navigation menu.");
        } else {
             analyticsRequest.getPreviousExecution(historyPlace.getAnalyticsExecutionId()).with(AnalyticsTaskExecutionProxy.EXECUTION_PROPERTIES).to(dashboardReceiver);;
        }

        return analyticsRequest;
    }
}