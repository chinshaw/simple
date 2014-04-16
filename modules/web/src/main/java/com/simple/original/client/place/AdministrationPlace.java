package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.api.domain.GroupMembership;

public class AdministrationPlace extends ApplicationPlace {

    public static class Tokenizer implements PlaceTokenizer<AdministrationPlace> {
        @Override
        public String getToken(AdministrationPlace place) {
            return "";
        }

        @Override
        public AdministrationPlace getPlace(String token) {
            return new AdministrationPlace();
        }
    }

    public String getApplicationTitle() {
        return "Application Health";
    }
    
    public GroupMembership getRequiredMembershipAccess() {
        return GroupMembership.ADMINISTRATOR;
    }
}
