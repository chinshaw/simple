package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class MobileNavigationPlace extends ApplicationPlace {

	public static class Tokenizer implements PlaceTokenizer<MobileNavigationPlace> {
		@Override
		public String getToken(MobileNavigationPlace place) {
			return "";
		}

		@Override
		public MobileNavigationPlace getPlace(String token) {
			return new MobileNavigationPlace();
		}
	}
}
