package com.simple.original.client.service.event;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * This event is fired when the comet delivers a new entity message
 * 
 * @author valva
 * 
 */
public class EntityCreateEvent extends GwtEvent<EntityCreateEvent.Handler> {

	/**
	 * Handler for {@link EntityCreateEvent}. Implemented by handlers of
	 * EntityCreateEvent.
	 * 
	 * @author valva
	 * 
	 */
	public interface Handler extends EventHandler {

		/**
		 * Called when {@link EntityCreateEvent} is fired. This happens when a
		 * new entity message is received from comet
		 * 
		 * @param event
		 *            {@link EntityCreateEvent}
		 */
		void onEntityCreated(EntityCreateEvent event);
	}

	public static final Type<EntityCreateEvent.Handler> TYPE = new Type<EntityCreateEvent.Handler>();

	/**
	 * Class name of the proxy associated with the entity
	 */
	private String entityName;

	/**
	 * List of new ids received
	 */
	private Set<Long> ids;

	/**
	 * Constructs a EntityCreateEvent with proxy name and list of id's
	 * 
	 * @param entityName
	 *            Name of the proxy class corresponding to the entity
	 * @param ids
	 *            List of entity id's
	 */
	public EntityCreateEvent(String entityName, Set<Long> ids) {
		this.entityName = entityName;
		this.ids = ids;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onEntityCreated(this);
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @return the ids
	 */
	public Set<Long> getIds() {
		return ids;
	}
}
