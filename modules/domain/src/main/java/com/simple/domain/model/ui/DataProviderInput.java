package com.simple.domain.model.ui;

import javax.persistence.Entity;

import com.simple.api.orchestrator.IDataProviderInput;
import com.simple.domain.model.RequestFactoryEntity;

@Entity
public class DataProviderInput extends RequestFactoryEntity implements IDataProviderInput {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -4480884266005381291L;
	
	/**
	 * The type of the data provider
	 */
	private Type type;
	
	/**
	 * Text description of the dataprovider, this is used to explain what is needed
	 * when building the task.
	 */
	private String description;

	/**
	 * Get the type of the data provider
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Set the type of the data provider
	 * @param type
	 */
	public void setType(Type type) {
		this.type = type;
	}


	/**
	 * Get the description of the data provider.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the input.
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
