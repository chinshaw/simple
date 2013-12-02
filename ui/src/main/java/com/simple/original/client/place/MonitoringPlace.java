package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends DashBoardPlace so that it draws the dashboard activity.
 * @author chinshaw
 *
 */
public class MonitoringPlace extends ApplicationPlace {
	
	
	public MonitoringPlace() {
		
	}
	
	
	public static class Tokenizer implements PlaceTokenizer<MonitoringPlace> {
		@Override
		public String getToken(MonitoringPlace place) {
			return "";
		}

		@Override
		public MonitoringPlace getPlace(String token) {
			return new MonitoringPlace();
		}
	}
    /**
     * This method is used to display the title of the page. 
     */
	public String getApplicationTitle() {
	    return "Alert Management";
	}
	
}
