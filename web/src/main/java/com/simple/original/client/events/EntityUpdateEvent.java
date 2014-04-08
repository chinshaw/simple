package com.simple.original.client.events;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * This event is fired when the comet delivers updated entity message
 * 
 * @author valva
 *
 */
public class EntityUpdateEvent extends GwtEvent<EntityUpdateEvent.Handler> {
	
	/**
	 * Handler for {@link EntityUpdateEvent}. Implemented by handlers of EntityUpdateEvent.
	 * 
	 * @author valva
	 *
	 */
	public interface Handler extends EventHandler {
		  
	    /**
	     * Called when {@link EntityUpdateEvent} is fired. This happens when a 
	     * updated entity message is received from comet
	     * 
	     * @param event {@link EntityUpdateEvent}
	     */
		void onEntityUpdated(EntityUpdateEvent event);
	    
	  }

	public static final Type<EntityUpdateEvent.Handler> TYPE = new Type<EntityUpdateEvent.Handler>();
	
	/**
	 * Class name of the proxy associated with the entity
	 */
	private String entityName;
	
	/**
	 * List of ids received as updated
	 */
	private Set<Long> ids;

	/**
	 * Constructs a EntityUpdateEvent with proxy name and list of id's 
	 * 
	 * @param entityName Name of the proxy class corresponding to the entity
	 * @param ids	List of entity id's
	 */
	public EntityUpdateEvent(String entityName, Set<Long> ids) {
		this.entityName = entityName;
		this.ids = ids;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
	  return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onEntityUpdated(this);
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
