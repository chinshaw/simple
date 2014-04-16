package com.simple.original.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.simple.original.client.proxy.AnalyticsTaskProxy;
import com.simple.original.client.proxy.DatastoreObjectProxy;

/**
 * Must extends DashBoardPlace so that it draws the dashboard activity.
 * 
 * @author chinshaw
 * 
 */
public class AnalyticsTaskBuilderPlace extends ApplicationPlace {

	public enum TaskMode {
		CREATE_EDIT(AnalyticsTaskProxy.class), 
		COPY(AnalyticsTaskProxy.class);
		
		private Class<? extends DatastoreObjectProxy> entityType;
		TaskMode(Class<? extends DatastoreObjectProxy> entityType) {
			this.entityType = entityType;
		}
		
		public Class<? extends DatastoreObjectProxy> getEntityType() {
			return entityType;
		}
	}
	
    private Long analyticsTaskId;

    public AnalyticsTaskBuilderPlace(Long analyticsTaskId) {
        this.analyticsTaskId = analyticsTaskId;
    }

    public Long getAnalyticsTaskId() {
        return analyticsTaskId;
    }
    
    public TaskMode getMode() {
    	return TaskMode.CREATE_EDIT;
    }

    public static class Tokenizer implements PlaceTokenizer<AnalyticsTaskBuilderPlace> {
        @Override
        public String getToken(AnalyticsTaskBuilderPlace place) {
            Long analyticsTaskId = place.getAnalyticsTaskId();
            if (analyticsTaskId == null) {
                return "";
            }
            return analyticsTaskId.toString();
        }

        @Override
        public AnalyticsTaskBuilderPlace getPlace(String token) {
            return new AnalyticsTaskBuilderPlace(PlaceUtils.longFromToken(token));
        }
    }
    
    public Class<? extends AnalyticsTaskProxy> getEntityType() {
    	return AnalyticsTaskProxy.class;
    }

    @Override
    public String getApplicationTitle() {
        return "Create/Edit Analytics Task";
    }
    
}
