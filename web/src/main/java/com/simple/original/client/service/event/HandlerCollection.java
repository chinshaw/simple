package com.simple.original.client.service.event;

import java.util.HashSet;
import java.util.Iterator;

import com.google.web.bindery.event.shared.HandlerRegistration;

public class HandlerCollection extends HashSet<HandlerRegistration>  {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -7265252261906514443L;

	
	/**
	 * Cleanup will first remove the handlers from the handler manager and then
	 * remove the handlers form the Set. 
	 */
	public void cleanup() {
		for (Iterator<HandlerRegistration> iter = iterator(); iter.hasNext();) {
			iter.next().removeHandler();
		}
		super.clear();
	}
	
	@Override
	public void clear() {
		cleanup();
	}
}