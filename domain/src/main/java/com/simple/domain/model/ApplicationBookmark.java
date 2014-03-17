package com.simple.domain.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.simple.original.api.orchestrator.IApplicationBookmark;

/**
 * Bookmark is a user defined location that has a name and an address
 * 
 * @author chinshaw
 */
@Entity
public class ApplicationBookmark extends RequestFactoryEntity implements IApplicationBookmark {
	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 8247257967370763471L;

	/**
	 * The name of the bookmark defined by the user.
	 */
	@Size(min = 5, max = 40, message = "name of bookmark must be between 5 and 40 characters")
	private String name;

	/**
	 * The place where the bookmark will take you.
	 */
	@NotNull
	private String placeToken;

	/**
	 * Get the name of the bookmark defined by the user
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWhere() {
		return placeToken;
	}

	public void setWhere(String placeToken) {
		this.placeToken = placeToken;
	}
}
