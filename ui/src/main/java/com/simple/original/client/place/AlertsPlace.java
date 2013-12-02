package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * FactoryAlertsPlace will allow you to view alerts for a specific factory or
 * all alerts.
 * 
 * @author chinshaw
 */
public class AlertsPlace extends ApplicationPlace {

    /**
     * Id of the factory we will be fetching alerts for.
     */
    private Long factoryId;

    public AlertsPlace() {
        this(null);
    }

    /**
     * Default constructor. The factory id of the factory you wish to show
     * alerts for or null to show alerts for all factories.
     * 
     * @param factoryId
     */
    public AlertsPlace(Long factoryId) {
        this.factoryId = factoryId;
    }

    /**
     * Getter for the factory id that is being viewed.
     * 
     * @return Fatory unique id.
     */
    public Long getFactoryId() {
        return factoryId;
    }

    /**
     * Tokenizer class to tokenize the place for history.
     * 
     * @author chinshaw
     * 
     */
    public static class Tokenizer implements PlaceTokenizer<AlertsPlace> {
        @Override
        public String getToken(AlertsPlace place) {
            if (place.getFactoryId() == null) {
                return "";
            }
            return place.getFactoryId().toString();
        }

        @Override
        public AlertsPlace getPlace(String token) {
            Long factoryId;

            try {
                factoryId = Long.parseLong(token);
            } catch (NumberFormatException e) {
                factoryId = null;
            }

            return new AlertsPlace(factoryId);
        }
    }

    
    @Override
    public String getApplicationTitle() {
        return "Factory Alerts";
    }
}
