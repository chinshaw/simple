package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class WelcomePlace extends ApplicationPlace {

	public WelcomePlace() {
	}

	public static class Tokenizer implements PlaceTokenizer<WelcomePlace> {
		@Override
		public String getToken(WelcomePlace place) {
			return "";
		}

		@Override
		public WelcomePlace getPlace(String token) {
			return new WelcomePlace();
		}
	}

	public String getApplicationTitle() {
		return "Welcome";
	}

}
