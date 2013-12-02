package com.simple.original.client.place;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.client.proxy.DashboardProxy;

public class DashboardDesignerPlace extends ApplicationPlace {

	private static final String DASHBOARD_ID = "d";

	private final Long dashboardId;

	private DashboardProxy dashboard;

	public DashboardDesignerPlace(Long dashboardId) {
		this.dashboardId = dashboardId;
	}

	public Long getDashboardId() {
		return dashboardId;
	}

	public DashboardProxy getDashboard() {
		return dashboard;
	}

	static class Tokenizer implements PlaceTokenizer<DashboardDesignerPlace> {
		@Override
		public String getToken(DashboardDesignerPlace place) {
			JSONObject jso = new JSONObject();
			if (place.getDashboardId() != null) {
				jso.put(DASHBOARD_ID, new JSONString(place.getDashboardId().toString()));
			}
			return jso.toString();
		}

		@Override
		public DashboardDesignerPlace getPlace(String token) {
			DashboardDesignerPlace place = null;
			if (token != null && !token.isEmpty()) {
				Map<String, String> map = PlaceUtils.StringToHashMap(token);

				Long dashboardId = PlaceUtils.longFromToken(map.get(DASHBOARD_ID));
				place = new DashboardDesignerPlace(dashboardId);
			}
			return place;
		}
	}

	@Override
	public String getApplicationTitle() {
		return "Dashboard Designer";
	}

	@Override
	public GroupMembership getRequiredMembershipAccess() {
		return GroupMembership.ADMINISTRATOR;
	}
}