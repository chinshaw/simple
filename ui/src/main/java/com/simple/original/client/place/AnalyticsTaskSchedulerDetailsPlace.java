package com.simple.original.client.place;

import java.util.HashMap;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.original.client.proxy.QuartzTriggerKeyProxy;

public class AnalyticsTaskSchedulerDetailsPlace extends AnalyticsTaskSchedulerPlace {

	private static final String TriggerGroup = "tg";
	private static final String TriggerName = "n";

	private String triggerGroup;
	private String triggerName;
	

	public AnalyticsTaskSchedulerDetailsPlace(QuartzTriggerKeyProxy triggerKey) {
		this.triggerGroup = triggerKey.getGroup();
		this.triggerName = triggerKey.getName();
	}
	
	public AnalyticsTaskSchedulerDetailsPlace(String triggerGroup, String triggerName) {
		this.triggerGroup = triggerGroup;
		this.triggerName = triggerName;
	}
	
	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}



	public static class Tokenizer implements
			PlaceTokenizer<AnalyticsTaskSchedulerDetailsPlace> {
		@Override
		public String getToken(AnalyticsTaskSchedulerDetailsPlace place) {
			String token = "";
			if (place.getTriggerName() != null && place.getTriggerGroup() != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TriggerGroup, place.getTriggerGroup());
				map.put(TriggerName, place.getTriggerName());
				
				token = PlaceUtils.HashMapAsString(map);
			}
			
			return token;
		}

		@Override
		public AnalyticsTaskSchedulerDetailsPlace getPlace(String token) {
			AnalyticsTaskSchedulerDetailsPlace place = null;
			if (token != null) {
				HashMap<String,String> map = PlaceUtils.StringToHashMap(token);
				
				place = new AnalyticsTaskSchedulerDetailsPlace(map.get(TriggerGroup), map.get(TriggerName));
			}
			
			return place;
		}
	}

	@Override
	public String getApplicationTitle() {
		return "Schedule Analytics Task";
	}
}