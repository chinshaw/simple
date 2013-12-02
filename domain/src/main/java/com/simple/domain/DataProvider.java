 package com.simple.domain;

import javax.persistence.Entity;

import com.simple.original.api.analytics.IDataProvider;


/**
 * Base class for other data providers
 * @author chinshaw
 */
@Entity
public abstract class DataProvider extends RequestFactoryEntity implements IDataProvider {
    
    /**
     * Serialization Id.
     */
    private static final long serialVersionUID = 3812268354023597824L;
    
    private String description;
    
    /**
     * This is the R variable name that will be assigned in the operation.
     */
    protected String variableName;
    
    /* (non-Javadoc)
     * @see com.simple.original.server.domain.IDataProvider#getVariableName()
     */
    @Override
    public String getVariableName() {
        return variableName;
    }
    
    /* (non-Javadoc)
     * @see com.simple.original.server.domain.IDataProvider#setVariableName(java.lang.String)
     */
    @Override
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    /* (non-Javadoc)
     * @see com.simple.original.server.domain.IDataProvider#clone()
     */
    @Override
    public DataProvider clone() throws CloneNotSupportedException{
    	DataProvider dataProvider = (DataProvider) super.clone();
    	dataProvider.id = null;
    	dataProvider.version = 0;
    	return dataProvider;
    }
}