package com.simple.original.client.service.event;



import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.place.shared.PlaceChangeEvent;

@JsonIgnoreProperties(value ={"dead"})
public class JobCompletionEvent extends
		GwtEvent<JobCompletionEvent.Handler> {

	/**
	 * Implemented by handlers of PlaceChangeEvent.
	 */
	public interface Handler extends EventHandler {
		/**
		 * Called when a {@link PlaceChangeEvent} is fired.
		 * 
		 * @param event
		 *            the {@link PlaceChangeEvent}
		 */
		void onJobCompleted(JobCompletionEvent event);
	}

	/**
	 * A singleton instance of Type&lt;Handler&gt;.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();


	private String jobId;
	
	public JobCompletionEvent() {
		
	}
	
	public JobCompletionEvent(String jobId) {
		this.jobId = jobId;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onJobCompleted(this);
	}
	
	public String getJobId() {
		return jobId;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}
}
