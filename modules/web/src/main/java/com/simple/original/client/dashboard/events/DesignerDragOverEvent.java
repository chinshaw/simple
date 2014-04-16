package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.simple.original.client.dashboard.AbstractDashboardWidget;

public class DesignerDragOverEvent extends GwtEvent<DesignerDragOverEvent.Handler> {

	public interface Handler extends EventHandler {

		void onDragOver(DesignerDragOverEvent event);
	}

	/**
	 * Event type for drag start events. Represents the meta-data associated
	 * with this event.
	 */
	private static final Type<Handler> TYPE = new Type<Handler>();

	private final AbstractDashboardWidget<?> overWidget;

	private AbstractDashboardWidget<?> draggingWidget;

	public DesignerDragOverEvent(AbstractDashboardWidget<?> overWidget, AbstractDashboardWidget<?> draggingWidget) {
		this.overWidget = overWidget;
		this.draggingWidget = draggingWidget;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onDragOver(this);
	}

	public AbstractDashboardWidget<?> getDragOverWidget() {
		return overWidget;
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