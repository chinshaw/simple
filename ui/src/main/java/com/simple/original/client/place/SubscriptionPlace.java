package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class SubscriptionPlace extends ApplicationPlace {

	public SubscriptionPlace(){
		
	}

	public static class Tokenizer implements PlaceTokenizer<SubscriptionPlace> {
		@Override
		public String getToken(SubscriptionPlace place) {
			return "";
		}

		@Override
		public SubscriptionPlace getPlace(String token) {				
			return new SubscriptionPlace();
		}
	}

	@Override
	public String getApplicationTitle() {
	    return "Subscriptions";
	}
}
