package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class AlertsSubscriptionPlace extends SubscriptionPlace {

	public AlertsSubscriptionPlace(){
		
	}

	public static class Tokenizer implements PlaceTokenizer<AlertsSubscriptionPlace> {
		@Override
		public String getToken(AlertsSubscriptionPlace place) {
			return "";
		}

		@Override
		public AlertsSubscriptionPlace getPlace(String token) {				
			return new AlertsSubscriptionPlace();
		}
	}

	@Override
	public String getApplicationTitle() {
	    return "Alert Subscription";
	}
}
