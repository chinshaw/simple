/**
 * 
 */
package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * @author chris
 *
 */
public class OperationExecutionPlace extends ApplicationPlace {

	
	public static class Tokenizer implements PlaceTokenizer<OperationExecutionPlace> {

		@Override
		public OperationExecutionPlace getPlace(String token) {
			return new OperationExecutionPlace();
		}

		@Override
		public String getToken(OperationExecutionPlace place) {
			return "";
		}
		
	}
}
