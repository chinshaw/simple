package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends DashBoardPlace so that it draws the dashboard activity.
 * 
 * @author chinshaw
 * 
 */
public class LatestDashboardPlace extends ApplicationPlace {

	private Long dashboardId;

	/**
	 * Default constructor, the server will fill in the id for the dashboard
	 * place.
	 */
	public LatestDashboardPlace(Long dashboardId) {
		this.dashboardId = dashboardId;
	}

	public Long getDashboardId() {
		return dashboardId;
	}

	public static class Tokenizer implements
			PlaceTokenizer<LatestDashboardPlace> {
		@Override
		public String getToken(LatestDashboardPlace place) {
			return place.dashboardId.toString();
		}

		@Override
		public LatestDashboardPlace getPlace(String token) {
			return new LatestDashboardPlace(PlaceUtils.longFromToken(token));
		}
	}

	@Override
	public String getApplicationTitle() {
		return "Cached Task Execution";
	}
}