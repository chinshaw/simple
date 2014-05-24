package com.simple.domain.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.simple.api.orchestrator.IDatastoreObject;

@MappedSuperclass
public abstract class DatastoreObject implements IDatastoreObject, Serializable {

	/**
	 * Serialization id, this is mainly used for gwt rpc calls and events.
	 */
	private static final long serialVersionUID = -3952451929056111942L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	protected String getIdAsString() {
		return id.toString();
	}

}
