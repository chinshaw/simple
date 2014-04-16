package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class AnalyticsOperationPlace extends ApplicationPlace {
	
	 private Long analyticsOperationId;
	    
	 
	  public AnalyticsOperationPlace(Long analyticsOperationId) {
	        this.analyticsOperationId = analyticsOperationId;
	    }
	 
	  
	  /**
		 * @return the analyticsOperationId
		 */
		public Long getAnalyticsOperationId() {
			return analyticsOperationId;
		}


	  public static class Tokenizer implements PlaceTokenizer<AnalyticsOperationPlace> {
	        @Override
	        public String getToken(AnalyticsOperationPlace place) {
	        	if (place.getAnalyticsOperationId() != null) {
	            	return place.getAnalyticsOperationId().toString();
	            }	            
	            return null;
	        }

	        @Override
	        public AnalyticsOperationPlace getPlace(String token) {
	        	if(token == null){
	        		return new AnalyticsOperationPlace(null);
	        	}
	            Long analyticsOperationId = PlaceUtils.longFromToken(token);
	            return new AnalyticsOperationPlace(analyticsOperationId);
	        }
	    }

	    public String getApplicationTitle() {
	    	if(getAnalyticsOperationId() ==  null){
	    		return "Create AnalyticsOperation";
	    	}else{
	    		return "Edit AnalyticsOperation";
	    	}
	    }
}
