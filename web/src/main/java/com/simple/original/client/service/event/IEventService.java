package com.simple.original.client.service.event;

import java.net.MalformedURLException;

public interface IEventService {

	
	/**
	 * Used to start the event service.
	 * @throws MalformedURLException 
	 */
	void start() throws MalformedURLException;
	
	/**
	 * Used to stop the event service.
	 */
	void stop();
	
	/**
	 * Used to pause the event service.
	 */
	void pause();
}
