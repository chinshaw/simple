package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends DashBoardPlace so that it draws the dashboard activity.
 * @author chinshaw
 *
 */
public class AnalyticsTasksSchedulerPlace extends ApplicationPlace {
	
	
	public AnalyticsTasksSchedulerPlace() {
		
	}
	
	public static class Tokenizer implements PlaceTokenizer<AnalyticsTasksSchedulerPlace> {
		@Override
		public String getToken(AnalyticsTasksSchedulerPlace place) {
			return "";
		}

		@Override
		public AnalyticsTasksSchedulerPlace getPlace(String token) {
			return new AnalyticsTasksSchedulerPlace();
		}
	}

    @Override
    public String getApplicationTitle() {
        return "Scheduler";
    }
    
}
