package com.simple.original.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.api.domain.GroupMembership;

public class ApplicationPlace extends Place  {

	public static class Tokenizer implements PlaceTokenizer<ApplicationPlace> {
		@Override
		public String getToken(ApplicationPlace place) {
			return "";
		}

		@Override
		public ApplicationPlace getPlace(String token) {
			return new ApplicationPlace();
		}
	}
	
	public String getApplicationTitle() {
	    return "Virtual Factory";
	}
	
	public GroupMembership getRequiredMembershipAccess() {
		return GroupMembership.USER;
	}
}
