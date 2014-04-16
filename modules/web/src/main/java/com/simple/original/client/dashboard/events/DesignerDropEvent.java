package com.simple.original.client.dashboard.events;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import com.simple.original.client.dashboard.events.DesignerDropEvent.DesignerDropHandler;

public class DesignerDropEvent extends DesignerDragDropEventBase<DesignerDropHandler> {

	public interface DesignerDropHandler extends EventHandler {

		void onDrop(DesignerDropEvent event);
	}

	/**
	 * Event type for drop events. Represents the meta-data associated with this
	 * event.
	 */
	private static final Type<DesignerDropHandler> TYPE = new Type<DesignerDropHandler>("drop", new DesignerDropEvent());

	/**
	 * Gets the event type associated with drop events.
	 * 
	 * @return the handler type
	 */
	public static Type<DesignerDropHandler> getType() {
		return TYPE;
	}

	/**
	 * Protected constructor, use
	 * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers)}
	 * or
	 * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers, com.google.gwt.dom.client.Element)}
	 * to fire drop events.
	 */
	protected DesignerDropEvent() {
	}

	@Override
	public final Type<DesignerDropHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DesignerDropHandler handler) {
		handler.onDrop(this);
	}
}