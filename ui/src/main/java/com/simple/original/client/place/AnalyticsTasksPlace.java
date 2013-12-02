package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends TasksPlace 
 * @author Hemantha
 *
 */
public class AnalyticsTasksPlace extends TasksPlace {

	public AnalyticsTasksPlace(){
	}

	public static class Tokenizer implements PlaceTokenizer<AnalyticsTasksPlace> {
		@Override
		public String getToken(AnalyticsTasksPlace place) {
			return "";
		}

		@Override
		public AnalyticsTasksPlace getPlace(String token) {				
			return new AnalyticsTasksPlace();
		}
	}

	@Override
	public String getApplicationTitle() {
		return "Analytics Tasks";
	}
}
