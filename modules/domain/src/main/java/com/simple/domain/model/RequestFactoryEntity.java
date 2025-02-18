package com.simple.domain.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import com.simple.api.orchestrator.IRequestFactoryEntity;


/**
 * This is the coordinating implementation for the IRequestFactoryEntity
 * interface this maps directly to the {@link EntityProxy} interface for gwt.
 * 
 * @author chinshaw
 * 
 */
@MappedSuperclass
public abstract class RequestFactoryEntity extends DatastoreObject implements IRequestFactoryEntity {

    /**
     * Serialization id
     */
    private static final long serialVersionUID = -1358283075774224638L;

    protected Integer version = 0;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @PrePersist
    public void incrementVerison() {
        version += 1;
    }
     
    public RequestFactoryEntity clone() {
    	return new RequestFactoryEntity() {
		};
    }
}