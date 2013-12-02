package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * @author nallaraj
 * @description Place for Alert Definition Edit/Create screen
 */
public class AlertDefinitionEditPlace extends ApplicationPlace {

	/**
	 * Unique Id of the Alert Definition
	 */
	private Long alertDefinitionId;
	
	/**
	 * Title of the screen
	 */
	private String alertDefinitionTitle;
	
	/**
	 * Required default constructor
	 * @param alertDefinitionId
	 */
	public AlertDefinitionEditPlace(Long alertDefinitionId) {
		this.alertDefinitionId = alertDefinitionId;
		if(alertDefinitionId != null){
			this.alertDefinitionTitle = "Alert Modification";
		}else{
			this.alertDefinitionTitle = "Alert Creation";
		}
		
    }

	/** 
	 * Get the AlertDefinition Id
	 * @return
	 */
	public Long getAlertDefinitionId(){
		return alertDefinitionId;
	}

    public static class Tokenizer implements PlaceTokenizer<AlertDefinitionEditPlace> {
        @Override
        public String getToken(AlertDefinitionEditPlace place) {
            Long alertDefId = place.getAlertDefinitionId();
            if (alertDefId == null) {
                return "";
            }
            return alertDefId.toString();
        }

        @Override
        public AlertDefinitionEditPlace getPlace(String token) {
            return new AlertDefinitionEditPlace(PlaceUtils.longFromToken(token));
        }
    }

    @Override
    public String getApplicationTitle() {
        return alertDefinitionTitle;
    }

	
}
