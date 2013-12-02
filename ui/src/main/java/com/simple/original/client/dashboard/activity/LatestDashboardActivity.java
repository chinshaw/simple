package com.simple.original.client.dashboard.activity;

import com.google.inject.Inject;
import com.simple.original.client.place.LatestDashboardPlace;
import com.simple.original.client.proxy.AnalyticsTaskExecutionProxy;
import com.simple.original.client.service.ServiceRequestFactory.AnalyticsRequest;
import com.simple.original.client.view.IDashboardView;

public class LatestDashboardActivity extends DashboardActivity {

	@Inject
	public LatestDashboardActivity(IDashboardView view) {
		super(view);
	}

	@Override
	protected AnalyticsRequest getDashboardUpdateRequest() {
		LatestDashboardPlace lastExecutionPlace = (LatestDashboardPlace) place();
		AnalyticsRequest analyticsRequest = service()
				.analyticsRequest();
		if (lastExecutionPlace.getDashboardId() == null) {
			display.showError("Analytics task id is not specified in the path, check the url or use navigation menu.");
		} else {
			analyticsRequest
					.getLatestDashboard(lastExecutionPlace.getDashboardId())
					.with(AnalyticsTaskExecutionProxy.EXECUTION_PROPERTIES)
					.to(dashboardReceiver);
		}

		return analyticsRequest;
	}
}