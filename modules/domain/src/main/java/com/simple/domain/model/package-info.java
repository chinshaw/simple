/**
 * The domain package is root package where all the entity objects are kept. 
 * Almost every class in here is persisted in the datastore. We use the 
 * classes in the com.simple.original.server.dao package as accessors to retrieve these objects.
 * 
 * These classes also typically have a correlating client side proxy class in the com.simple.original.client.proxy location.
 * The exceptions may be the classes in the domain.dashboard classes where their proxies will be stored
 * in the com.simple.original.client.dashboard classes. They domain.dashboard and domain.metric pakcages exist
 * simply to break up the domain package into more granular packages.
 * 
 * If there is an entity bean in the database it should always be here with the exception of some of
 * the quartz classes and quartz handles storing those.
 */
package com.simple.domain.model;