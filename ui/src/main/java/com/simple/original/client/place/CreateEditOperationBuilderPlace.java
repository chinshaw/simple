package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class CreateEditOperationBuilderPlace extends ApplicationPlace {
	
	 private Long analyticsOperationId;
	    
	 
	  public CreateEditOperationBuilderPlace(Long analyticsOperationId) {
	        this.analyticsOperationId = analyticsOperationId;
	    }
	 
	  
	  /**
		 * @return the analyticsOperationId
		 */
		public Long getAnalyticsOperationId() {
			return analyticsOperationId;
		}


	  public static class Tokenizer implements PlaceTokenizer<CreateEditOperationBuilderPlace> {
	        @Override
	        public String getToken(CreateEditOperationBuilderPlace place) {
	        	if (place.getAnalyticsOperationId() != null) {
	            	return place.getAnalyticsOperationId().toString();
	            }	            
	            return null;
	        }

	        @Override
	        public CreateEditOperationBuilderPlace getPlace(String token) {
	        	if(token == null){
	        		return new CreateEditOperationBuilderPlace(null);
	        	}
	            Long analyticsOperationId = PlaceUtils.longFromToken(token);
	            return new CreateEditOperationBuilderPlace(analyticsOperationId);
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
