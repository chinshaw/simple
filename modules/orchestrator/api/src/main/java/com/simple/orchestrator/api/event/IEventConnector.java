package com.simple.orchestrator.api.event;

/**
 * Interface for a job eventing system, this can be implemented to handle events
 * destined to some kind of bus. We implement two types of connector. EventBus
 * for testing mainly and then jms for active mq integration.
 * @author chinshaw
 *
 */
public interface IEventConnector {

	public void subscribe(Object event);
	
	public void post(Object object);
}
