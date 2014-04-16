package com.simple.original.client.service;

import com.simple.original.client.proxy.DatastoreObjectProxy;

/**
 * Used to create a provider of a search request, this is mainly used to abstract
 * the view away from the activity.
 * @author chinshaw
 *
 * @param <T>
 */
public interface SearchRequestProvider<T extends DatastoreObjectProxy> {
    
    SearchableRequest<T> createSearchRequest();

}
