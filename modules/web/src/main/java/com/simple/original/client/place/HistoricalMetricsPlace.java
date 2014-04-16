package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends DashBoardPlace so that it draws the dashboard activity.
 * 
 * @author chinshaw
 * 
 */
public class HistoricalMetricsPlace extends ApplicationPlace {

    private final Long analyticsExecutionId;
    
    /**
     * Default constructor, the server will fill in the id for the dashboard place.
     */
    public HistoricalMetricsPlace(Long analyticsExecutionId) {
        this.analyticsExecutionId = analyticsExecutionId;
    }
    
    public Long getAnalyticsExecutionId() {
        return analyticsExecutionId;
    }

    public static class Tokenizer implements PlaceTokenizer<HistoricalMetricsPlace> {
        @Override
        public String getToken(HistoricalMetricsPlace place) {
            return place.getAnalyticsExecutionId().toString();
        }

        @Override
        public HistoricalMetricsPlace getPlace(String token) {
            return new HistoricalMetricsPlace(Long.parseLong(token));
        }
    }

    @Override
    public String getApplicationTitle() {
        return "Global Metrics";
    }
}