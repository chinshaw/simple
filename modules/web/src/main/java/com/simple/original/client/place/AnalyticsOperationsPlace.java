package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class AnalyticsOperationsPlace extends ApplicationPlace {

    public static class Tokenizer implements PlaceTokenizer<AnalyticsOperationsPlace> {
        @Override
        public String getToken(AnalyticsOperationsPlace place) {
            return "";
        }

        @Override
        public AnalyticsOperationsPlace getPlace(String token) {
            return new AnalyticsOperationsPlace();
        }
    }

    public String getApplicationTitle() {
        return "Analytics Builder";
    }
}
