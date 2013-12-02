package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends TaskBuilderPlace
 * @author Hemantha
 * 
 */
public class AnalyticsTaskCopyPlace extends AnalyticsTaskBuilderPlace {

	public AnalyticsTaskCopyPlace(Long taskId) {
		super(taskId);
	}
	
	@Override
	public TaskMode getMode() {
    	return TaskMode.COPY;
    }
	
	public static class Tokenizer implements PlaceTokenizer<AnalyticsTaskCopyPlace> {
		@Override
		public String getToken(AnalyticsTaskCopyPlace place) {
			return place.getAnalyticsTaskId().toString();
		}

		@Override
		public AnalyticsTaskCopyPlace getPlace(String token) {
			return new AnalyticsTaskCopyPlace(PlaceUtils.longFromToken(token));
		}
	}

	@Override
	public String getApplicationTitle() {
		return "Copy Analytics Task";
	}
}
