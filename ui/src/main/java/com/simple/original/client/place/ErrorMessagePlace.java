package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class ErrorMessagePlace extends ApplicationPlace {

	
	 static class Tokenizer implements PlaceTokenizer<ErrorMessagePlace> {
	        @Override
	        public String getToken(ErrorMessagePlace place) {
	            return "";
	        }

	        @Override
	        public ErrorMessagePlace getPlace(String token) {
	            return new ErrorMessagePlace();
	        }
	    }

	    public String getApplicationTitle() {
	        return "Error";
	    }

}
