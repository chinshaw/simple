package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.api.domain.GroupMembership;

public class DataProviderEditPlace extends ApplicationPlace {

    public static class Tokenizer implements PlaceTokenizer<DataProviderEditPlace> {
        @Override
        public String getToken(DataProviderEditPlace place) {
            return "";
        }

        @Override
        public DataProviderEditPlace getPlace(String token) {
            return new DataProviderEditPlace();
        }
    }

    public String getApplicationTitle() {
        return "Data Provider Configuration";
    }
    
    public GroupMembership getRequiredMembershipAccess() {
        return GroupMembership.ADMINISTRATOR;
    }
}
