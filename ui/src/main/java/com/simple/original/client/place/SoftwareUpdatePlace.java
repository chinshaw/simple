package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.original.api.domain.GroupMembership;

public class SoftwareUpdatePlace extends ApplicationPlace {

    static class Tokenizer implements PlaceTokenizer<SoftwareUpdatePlace> {
        @Override
        public String getToken(SoftwareUpdatePlace place) {
            return "";
        }

        @Override
        public SoftwareUpdatePlace getPlace(String token) {
            return new SoftwareUpdatePlace();
        }
    }

    public String getApplicationTitle() {
        return "Analytics Builder";
    }
    
    @Override
	public GroupMembership getRequiredMembershipAccess() {
		return GroupMembership.ADMINISTRATOR;
	}
}
