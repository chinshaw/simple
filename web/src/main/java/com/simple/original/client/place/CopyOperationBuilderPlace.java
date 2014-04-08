package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class CopyOperationBuilderPlace extends ApplicationPlace {
	
	 private Long analyticsOperationId;	 
	 
	   public CopyOperationBuilderPlace(Long analyticsOperationId) {
	        this.analyticsOperationId = analyticsOperationId;
	    }
	
		/**
		 * @return the analyticsOperationId
		 */
		public Long getAnalyticsOperationId() {
			return analyticsOperationId;
		}


	    public static class Tokenizer implements PlaceTokenizer<CopyOperationBuilderPlace> {
	        @Override
	        public String getToken(CopyOperationBuilderPlace place) {
	            return place.getAnalyticsOperationId().toString();
	        }

	        @Override
	        public CopyOperationBuilderPlace getPlace(String token) {
	            Long analyticsOperationId = PlaceUtils.longFromToken(token);
	            return new CopyOperationBuilderPlace(analyticsOperationId);
	        }
	    }

	    @Override
	    public String getApplicationTitle() {
	        return "Copy AnalyticsOperation";
	    }
	

}
