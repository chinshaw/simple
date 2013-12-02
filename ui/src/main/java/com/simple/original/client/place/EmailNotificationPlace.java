
package com.simple.original.client.place;


import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.original.client.proxy.AnalyticsTaskMonitorProxy;

public class EmailNotificationPlace extends ApplicationPlace {

	private static AnalyticsTaskMonitorProxy argument;
	
	public EmailNotificationPlace(AnalyticsTaskMonitorProxy arg) {
		argument = arg;
    }

    public static class Tokenizer implements PlaceTokenizer<EmailNotificationPlace> {
        @Override
        public String getToken(EmailNotificationPlace place) {
            return "";
        }
		@Override
		public EmailNotificationPlace getPlace(String token) {
			return null;			

		}
    }
	
	/**
 	* Used to retrive the values from alert definition proxy
 	*/
    public AnalyticsTaskMonitorProxy getArgs() {
		return  argument;
	}
	
	/**
 	* Used to retrive the title to be displayed on the screen
 	*/
    @Override
    public String getApplicationTitle() {
        return "Email Notification - "+getArgs().getName();
    }

}

