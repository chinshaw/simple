package com.simple.original.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class LoginPlace extends Place {

	public static class Tokenizer implements PlaceTokenizer<LoginPlace> {
		@Override
		public String getToken(LoginPlace place) {
			return "";
		}

		@Override
		public LoginPlace getPlace(String token) {
			return new LoginPlace();
		}
	}
}
