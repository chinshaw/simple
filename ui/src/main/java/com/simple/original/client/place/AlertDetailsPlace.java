package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class AlertDetailsPlace extends ApplicationPlace{

	private Long factoryAlertId;
	
	public AlertDetailsPlace(Long alertId) {
		factoryAlertId = alertId;
		}
	
	public Long getFactoryAlertId() {
		return factoryAlertId; 
	}
	
	public static class Tokenizer implements PlaceTokenizer<AlertDetailsPlace> {
		@Override
		public String getToken(AlertDetailsPlace place) {
			if(place.getFactoryAlertId() != null){
				return place.getFactoryAlertId().toString();
			}
			return null;
		}
	
		@Override
		public AlertDetailsPlace getPlace(String token) {
			return new AlertDetailsPlace(PlaceUtils.longFromToken(token));
		}
	}



	public String getApplicationTitle() {
	    return "Alert Details ";
	}

}
