package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends TasksPlace 
 * @author Hemantha
 *
 */
public class DashboardsPlace extends TasksPlace {

	public DashboardsPlace(){
	}

	public static class Tokenizer implements PlaceTokenizer<DashboardsPlace> {
		@Override
		public String getToken(DashboardsPlace place) {
			return "";
		}

		@Override
		public DashboardsPlace getPlace(String token) {				
			return new DashboardsPlace();
		}
	}

	@Override
	public String getApplicationTitle() {
		return "Dashboards";
	}
}
