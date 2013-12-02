package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

public class ReportAdminSubscriptionPlace extends ApplicationPlace {

	/**
	 * Unique Id of the Reporting Task
	 */
	private Long reportingTaskId;

	
	/**
	 * Required default constructor
	 * @param reportingTaskId
	 */
	public ReportAdminSubscriptionPlace(Long taskId) {
		this.reportingTaskId = taskId;
    }

	/** 
	 * Get the ReportingTask Id
	 * @return
	 */
	public Long getReportingTaskId(){
		return reportingTaskId;
	}

    public static class Tokenizer implements PlaceTokenizer<ReportAdminSubscriptionPlace> {
        @Override
        public String getToken(ReportAdminSubscriptionPlace place) {
            Long alertDefId = place.getReportingTaskId();
            if (alertDefId == null) {
                return "";
            }
            return alertDefId.toString();
        }

        @Override
        public ReportAdminSubscriptionPlace getPlace(String token) {
            return new ReportAdminSubscriptionPlace(PlaceUtils.longFromToken(token));
        }
    }

    @Override
    public String getApplicationTitle() {
        return "Report Subscription";
    }


}
