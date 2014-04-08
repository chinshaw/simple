package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.dashboard.AbstractDashboardWidget;

public class DesignerDragLeaveEvent extends GwtEvent<DesignerDragLeaveEvent.Handler> {

	public interface Handler extends EventHandler {

		void onDragLeave(DesignerDragLeaveEvent event);
	}

	/**
	 * Event type for drag start events. Represents the meta-data associated
	 * with this event.
	 */
	private static final Type<Handler> TYPE = new Type<Handler>();

	private final AbstractDashboardWidget<?> leaveWidget;

	private AbstractDashboardWidget<?> draggingWidget;

	public DesignerDragLeaveEvent(AbstractDashboardWidget<?> leaveWidget, AbstractDashboardWidget<?> draggingWidget) {
		this.leaveWidget = leaveWidget;
		this.draggingWidget = draggingWidget;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onDragLeave(this);
	}

	public AbstractDashboardWidget<?> getLeavingWidget() {
		return leaveWidget;
	}

	public AbstractDashboardWidget<?> getDraggingWidget() {
		return draggingWidget;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	public static Type<Handler> getType() {
		return TYPE;
	}

}