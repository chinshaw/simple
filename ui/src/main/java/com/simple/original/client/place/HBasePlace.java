package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends TaskExecPlace 
 * @author chinshaw
 *
 */
public class HBasePlace extends ApplicationPlace  {
	
	public HBasePlace(){
	}

	public static class Tokenizer implements PlaceTokenizer<HBasePlace> {
		@Override
		public String getToken(HBasePlace place) {
			return "";
		}

		@Override
		public HBasePlace getPlace(String token) {
			return new HBasePlace();
		}
	}


    @Override
    public String getApplicationTitle() {
        return "HBase";
    }
}
