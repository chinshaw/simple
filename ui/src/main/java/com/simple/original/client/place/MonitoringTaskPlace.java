package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.original.api.domain.GroupMembership;

public class MonitoringTaskPlace  extends ApplicationPlace {

	public MonitoringTaskPlace() {

	}

	public static class Tokenizer implements PlaceTokenizer<MonitoringTaskPlace> {
		@Override
		public String getToken(MonitoringTaskPlace place) {
			return "";
		}

		@Override
		public MonitoringTaskPlace getPlace(String token) {
			return new MonitoringTaskPlace();
		}
	}

	/**
	 * This method is used to display the title of the page.
	 */
	public String getApplicationTitle() {
		return "Monitoring Task Limit Administration";
	}


    @Override
    public GroupMembership getRequiredMembershipAccess() {
        return GroupMembership.ADMINISTRATOR;
	}

}
