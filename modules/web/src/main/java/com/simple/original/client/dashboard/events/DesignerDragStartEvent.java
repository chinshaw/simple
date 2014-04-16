package com.simple.original.client.dashboard.events;

import com.google.gwt.event.shared.EventHandler;
import com.simple.original.client.dashboard.events.DesignerDragStartEvent.DesignerDragStartHandler;

public class DesignerDragStartEvent extends DesignerDragDropEventBase<DesignerDragStartHandler> {

	public interface DesignerDragStartHandler extends EventHandler {

		void onDragStart(DesignerDragStartEvent event);
	}

	/**
	 * Event type for drag start events. Represents the meta-data associated
	 * with this event.
	 */
	private static final Type<DesignerDragStartHandler> TYPE = new Type<DesignerDragStartHandler>("dragstart", new DesignerDragStartEvent());

	protected DesignerDragStartEvent() {
	}

	@Override
	public Type<DesignerDragStartHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DesignerDragStartHandler handler) {
		handler.onDragStart(this);
	}

	/**
	 * Gets the event type associated with drag start events.
	 * 
	 * @return the handler type
	 */
	public static Type<DesignerDragStartHandler> getType() {
		return TYPE;
	}
}