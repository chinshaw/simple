package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class ReportUserSubscriptionPlace extends SubscriptionPlace {

	public ReportUserSubscriptionPlace(){
		
	}

	public static class Tokenizer implements PlaceTokenizer<ReportUserSubscriptionPlace> {
		@Override
		public String getToken(ReportUserSubscriptionPlace place) {
			return "";
		}

		@Override
		public ReportUserSubscriptionPlace getPlace(String token) {				
			return new ReportUserSubscriptionPlace();
		}
	}

	@Override
	public String getApplicationTitle() {
	    return "Report Subscription";
	}
}
