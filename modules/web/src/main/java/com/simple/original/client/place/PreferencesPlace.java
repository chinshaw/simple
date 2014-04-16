package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends DashBoardPlace so that it draws the dashboard activity.
 * 
 * @author chinshaw
 * 
 */
public class PreferencesPlace extends ApplicationPlace {

    private Long personId = null;

    
    public PreferencesPlace() {
        this(null); // for good measure.
    }
    
    public PreferencesPlace(Long personId) {
        this.personId = personId;
    }

    public Long getPersonId() {
        return this.personId;
    }

    static class Tokenizer implements PlaceTokenizer<PreferencesPlace> {
        @Override
        public String getToken(PreferencesPlace place) {
            Long personId = place.getPersonId();
            if (personId == null) {
                return "";
            }
            return personId.toString();
        }

        @Override
        public PreferencesPlace getPlace(String token) {
            Long factoryId = null;

            try {
                factoryId = Long.parseLong(token);
            } catch (NumberFormatException e) {
                factoryId = null;
            }

            return new PreferencesPlace(factoryId);
        }
    }

    public String getApplicationTitle() {
        return "Preferences";
    }
}
