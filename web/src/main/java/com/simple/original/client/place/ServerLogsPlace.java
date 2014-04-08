package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends DashBoardPlace so that it draws the dashboard activity.
 * @author chinshaw
 *
 */
public class ServerLogsPlace extends ApplicationPlace {
	
	
	public ServerLogsPlace() {
		
	}
	
	
	public static class Tokenizer implements PlaceTokenizer<ServerLogsPlace> {
		@Override
		public String getToken(ServerLogsPlace place) {
			return "";
		}

		@Override
		public ServerLogsPlace getPlace(String token) {
			return new ServerLogsPlace();
		}
	}

	@Override
	public String getApplicationTitle() {
	    return "Debugging Tasks";
	}
}
