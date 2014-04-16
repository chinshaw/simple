package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Must extends ApplicationPlace 
 * @author Hemantha
 *
 */
public class TasksPlace extends ApplicationPlace {

	public TasksPlace(){
	}

	public static class Tokenizer implements PlaceTokenizer<TasksPlace> {
		@Override
		public String getToken(TasksPlace place) {
			return "";
		}

		@Override
		public TasksPlace getPlace(String token) {
			return new TasksPlace();
		}
	}

	@Override
	public String getApplicationTitle() {
		return "Analytics/Reporting Tasks";
	}
}

