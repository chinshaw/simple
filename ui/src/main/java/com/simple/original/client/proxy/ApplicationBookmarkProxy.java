package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.ApplicationBookmark;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

/**
 * Proxy for the {@link Bookmark} class.
 * 
 * @author chinshaw
 * 
 */
@ProxyFor(value = ApplicationBookmark.class, locator = RequestFactoryEntityLocator.class)
public interface ApplicationBookmarkProxy extends DatastoreObjectProxy {
	
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
	public String getWhere();

	/**
	 * Sets the location of the bookmark.
	 * 
	 * @param place
	 */
	public void setWhere(String place);

}
