package com.simple.domain.model.dataprovider;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.simple.domain.model.RequestFactoryEntity;
import com.simple.original.api.orchestrator.IDataProvider;

/**
 * Base class for other data providers
 * 
 * @author chinshaw
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlTransient
@XmlSeeAlso({ DBDataProvider.class, HttpDataProvider.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value=HttpDataProvider.class, name="HttpDataProvider"),
    @JsonSubTypes.Type(value=DBDataProvider.class, name="DBDataProvider")
})
public abstract class DataProvider extends RequestFactoryEntity implements
		IDataProvider {

	/**
	 * Serialization Id.
	 */
	private static final long serialVersionUID = 3812268354023597824L;

	private String description;

	/**
	 * This is the R variable name that will be assigned in the operation.
	 */
	protected String variableName;

	private boolean isRequired;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.server.domain.IDataProvider#getVariableName()
	 */
	@Override
	public String getVariableName() {
		return variableName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.server.domain.IDataProvider#setVariableName(java.
	 * lang.String)
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

	/**
	 * Is this data provider required for the operation to complete.
	 * 
	 * @return
	 */
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * Set whether this operation is required to complete.
	 * 
	 * @param isRequired
	 */
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.server.domain.IDataProvider#clone()
	 */
	@Override
	public DataProvider clone() throws CloneNotSupportedException {
		DataProvider dataProvider = (DataProvider) super.clone();
		dataProvider.id = null;
		dataProvider.version = 0;
		return dataProvider;
	}
}