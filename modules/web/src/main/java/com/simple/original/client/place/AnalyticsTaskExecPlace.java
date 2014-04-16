package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends TaskExecPlace 
 * @author chinshaw
 *
 */
public class AnalyticsTaskExecPlace extends ApplicationPlace  {
	
	public AnalyticsTaskExecPlace(){
	}

	public static class Tokenizer implements PlaceTokenizer<AnalyticsTaskExecPlace> {
		@Override
		public String getToken(AnalyticsTaskExecPlace place) {
			return "";
		}

		@Override
		public AnalyticsTaskExecPlace getPlace(String token) {
			return new AnalyticsTaskExecPlace();
		}
	}


    @Override
    public String getApplicationTitle() {
        return "Run Analytics Task";
    }
}
