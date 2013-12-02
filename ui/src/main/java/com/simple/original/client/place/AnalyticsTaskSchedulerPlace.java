package com.simple.original.client.place;

import java.util.List;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.QuartzTriggerKeyProxy;


public class AnalyticsTaskSchedulerPlace extends ApplicationPlace {

    private Long analyticsTaskId;
    private List<AnalyticsOperationInputProxy> inputs;
    private QuartzTriggerKeyProxy triggerKey;
    
    public AnalyticsTaskSchedulerPlace() {
        super();
    }

    public AnalyticsTaskSchedulerPlace(Long analyticsTaskId) {
        this.analyticsTaskId = analyticsTaskId;
    }
    
    public AnalyticsTaskSchedulerPlace(Long analtyicsTaskId, List<AnalyticsOperationInputProxy> inputs) {
        this.analyticsTaskId = analtyicsTaskId;
        this.inputs = inputs;
    }
    
    public AnalyticsTaskSchedulerPlace(Long analyticsTaskId, QuartzTriggerKeyProxy triggerKey) {
        this.analyticsTaskId = analyticsTaskId;
        this.triggerKey = triggerKey;
    }
    

    public Long getAnalyticsTaskId() {
        return this.analyticsTaskId;
    }
    
    public List<AnalyticsOperationInputProxy> getInputs() {
        return inputs;
    }
    
    public QuartzTriggerKeyProxy getScheduleKey() {
    	return triggerKey;
    }

    public static class Tokenizer implements PlaceTokenizer<AnalyticsTaskSchedulerPlace> {
        @Override
        public String getToken(AnalyticsTaskSchedulerPlace place) {
            Long analyticsTaskId = place.getAnalyticsTaskId();
            if (analyticsTaskId == null) {
                return "";
            }

            return analyticsTaskId.toString();
        }

        @Override
        public AnalyticsTaskSchedulerPlace getPlace(String token) {
            Long analyticsTaskId;

            try {
                analyticsTaskId = Long.parseLong(token);
            } catch (NumberFormatException e) {
                analyticsTaskId = null;
            }

            return new AnalyticsTaskSchedulerPlace(analyticsTaskId);
        }
    }
    
    @Override
    public String getApplicationTitle() {
        return "Schedule Analytics Task";
    }
}