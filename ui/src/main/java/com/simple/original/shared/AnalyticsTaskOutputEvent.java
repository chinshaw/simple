package com.simple.original.shared;

import java.io.Serializable;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.simple.domain.model.AnalyticsTask;
import com.simple.domain.model.AnalyticsTaskExecution;

/**
 * This is used to transport the entity change data to connected clients over
 * comet.
 * 
 * @author chinshaw
 */
public class AnalyticsTaskOutputEvent extends GwtEvent<AnalyticsTaskOutputEvent.Handler> implements Serializable, IsSerializable {

	private static final long serialVersionUID = -8159429361234347124L;

	public static final int STDOUT = 1;
	
	public static final int STDERR = 2;
	
	
	public interface Handler extends EventHandler   {
		void handleOut(String output);
		
		void handleError(String error);
	}
	
	/**
	 * The id of the {@link AnalyticsTask}
	 */
	private int device;

	/**
	 * The id of the {@link AnalyticsTaskExecution}
	 */
	private String output;
	
    /**
     * A singleton instance of Type&lt;Handler&gt;.
     */
    public static final Type<Handler> TYPE = new Type<Handler>();

    public AnalyticsTaskOutputEvent() {
    	this(-1, "");
    }
    
	public AnalyticsTaskOutputEvent(int device, String output) {
	    this.device = device;
	    this.output = output;
	}

	/**
	 * Getter for the id of the {@link AnalyticsTask}
	 * 
	 * @return
	 */
	public String getOutput() {
		return output;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
	    if (device == STDOUT) {
	        handler.handleOut(output);
	    } else if (device == STDERR) {
	        handler.handleError(output);
	    }
	}
}
