package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.original.api.domain.GroupMembership;

public class DataProvidersPlace extends ApplicationPlace {

    public static class Tokenizer implements PlaceTokenizer<DataProvidersPlace> {
        @Override
        public String getToken(DataProvidersPlace place) {
            return "";
        }

        @Override
        public DataProvidersPlace getPlace(String token) {
            return new DataProvidersPlace();
        }
    }

    public String getApplicationTitle() {
        return "Data Provider Configuration";
    }
    
    public GroupMembership getRequiredMembershipAccess() {
        return GroupMembership.ADMINISTRATOR;
    }
}
