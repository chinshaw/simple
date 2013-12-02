package com.simple.original.api.analytics;

/**
 * This interface is all that is needed in order to persist an object to the
 * database. By default we use Long as the ID and the table strategy so the id
 * will be unique for that table.
 * 
 * @author chinshaw
 * 
 */
public interface IDatastoreObject {

    /**
     * The unique id of the object from the datastore. This is guaranteed to be
     * unique for the type of object that implements this interface.
     * 
     * @return
     */
    public Long getId();

}