package com.simple.original.shared;

import java.io.Serializable;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * This is used to transport the entity change data to connected clients over comet. 
 * 
 * @author valva
 *
 */
public class CometEntity implements Serializable, IsSerializable {

	
	private static final long serialVersionUID = -8159429361234347124L;

	/**
	 * Set to true if notifying an update to existing entity
	 */
	private boolean update = false;
	
	/**
	 * Set to true if notifying a new entity creation
	 */
	private boolean create = false;
	
	/**
	 * Name of the entity. i.e. entity.getClass().getSimpleName()
	 */
	private String entityName;
	
	/**
	 * List of entity id's
	 */

	private Set<Long> ids;

	
	
	
	public CometEntity() {
	}

	
	/**
	 * Constructs a CometEntity with entity name, list of id's and either create or update set to true
	 *  
	 * @param update Set to true if notifying updates to existing entities
	 * @param create Set to true if notifying creation of new entities
	 * @param entityName Name of the entity. i.e. entity.getClass().getSimpleName()
	 * @param ids List of entity id's
	 */
	public CometEntity(boolean update, boolean create, String entityName, Set<Long> ids) {

		this.update = update;
		this.create = create;
		this.entityName = entityName;
		this.ids = ids;
		
		if(update && create) {
			throw new IllegalArgumentException("Both \"update\" and \"create\" can't be set to true");
		}
	}



	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(boolean update) {
		if(create) {
			throw new IllegalArgumentException("The attribute \"update\" can't be set to true when create is already true");
		}

		this.update = update;
	}

	/**
	 * @return the create
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * @param create the create to set
	 */
	public void setCreate(boolean create) {
		if(update) {
			throw new IllegalArgumentException("The attribute \"create\" can't be set to true when update is already true");
		}
		this.create = create;
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set. i.e. entity.getClass().getSimpleName()
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the ids
	 */
	public Set<Long> getIds() {
		return ids;
	}

	/**
	 * @param ids the ids to set
	 */
	public void setIds(Set<Long> ids) {
		this.ids = ids;
	}
}
