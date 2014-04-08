package com.simple.original.shared;

import java.io.Serializable;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.simple.domain.model.AnalyticsTask;

/**
 * This is used to transport the entity change data to connected clients over
 * comet.
 * 
 * @author chinshaw
 */
public class AnalyticsTaskExecutionEvent extends GwtEvent<AnalyticsTaskExecutionEvent.Handler> implements Serializable, IsSerializable {

	private static final long serialVersionUID = -8159429361234347124L;

	
	public interface Handler extends EventHandler   {
		void onAnalyticsTaskExecution(AnalyticsTaskExecutionEvent event);
	}
	
	/**
	 * The id of the {@link AnalyticsTask}
	 */
	private Long taskId;

	/**
	 * The id of the {@link AnalyticsTaskExecution}
	 */
	private Long taskExecutionId;
	
    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();

    public AnalyticsTaskExecutionEvent() {
    	this(null, null);
    }
    
	public AnalyticsTaskExecutionEvent(Long taskId, Long taskExecutionId) {
		this.taskId = taskId;
		this.taskExecutionId = taskExecutionId;
	}

	/**
	 * Getter for the id of the {@link AnalyticsTask}
	 * 
	 * @return
	 */
	public Long getTaskId() {
		return taskId;
	}

	/**
	 * Getter for the id of the {@link AnalyticsTaskExecution}
	 * 
	 * @return
	 */
	public Long getTaskExecutionId() {
		return taskExecutionId;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onAnalyticsTaskExecution(this);
	}
}
