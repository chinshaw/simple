/**
 * The dao package is used to store classes that will retrieve domain objects. This is a pattern we use to typically 
 * extract away the storage from the domain objects themselves. Not every entity type has it's own dao and this 
 * is because not all datastore objects are root level objects. The DaoBase class is the basic of all the dao class
 * and contains the base methods that are relavant for all datastore entities such as {@link DaoBase#save(IDatastoreObject),
 * {@link DaoBase#delete(Long)} and {@link DaoBase#find(Long)}
 * 
 * All entities that can be saved in the database extend the IDatastoreObject and have a unique Long id.
 * 
 * 
 * @link http://en.wikipedia.org/wiki/Data_access_object
 */
package com.simple.domain.dao;
