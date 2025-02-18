package com.simple.api.domain;

public interface IApplicationBookmark {

	/**
	 * The is the user defined name of the bookmar.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Sets the user defined name of the bookmark.
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * The place where the bookmark will take you.
	 * 
	 * @return
	 */
	public String getPlaceToken();

	/**
	 * Sets the location of the bookmark.
	 * 
	 * @param place
	 */
	public void setPlaceToken(String place);
}
