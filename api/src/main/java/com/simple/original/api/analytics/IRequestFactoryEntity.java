package com.simple.original.api.analytics;

/**
 * This interface allows an object to be sent using request factory. These 
 * are the required methods that are needed by the gwt EntityProxy  interface.
 * 
 * @see com.google.web.bindery.requestfactory.shared.EntityProxy
 * @author chinshaw
 *
 */
public interface IRequestFactoryEntity extends IDatastoreObject {

    /**
     * The version is required for the entity proxy so that requestfactory
     * can identify whether the object has been modified.
     * @return
     */
    public Integer getVersion();

    /**
     * This is for requestfactory to modify the version and should probably not be used
     * by user to modify version. This is typically transient in the implementing
     * objct.
     * @param version
     */
    public void setVersion(Integer version);
}
