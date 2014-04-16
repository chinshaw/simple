package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends TaskExecPlace 
 * @author chinshaw
 *
 */
public class OrchestratorPlace extends ApplicationPlace  {
	
	public OrchestratorPlace(){
	}

	public static class Tokenizer implements PlaceTokenizer<OrchestratorPlace> {
		@Override
		public String getToken(OrchestratorPlace place) {
			return "";
		}

		@Override
		public OrchestratorPlace getPlace(String token) {
			return new OrchestratorPlace();
		}
	}


    @Override
    public String getApplicationTitle() {
        return "HBase";
    }
}
