package com.simple.domain.model.ui;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.simple.api.orchestrator.IAnalyticsOperationInput;
import com.simple.domain.model.RequestFactoryEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
@XmlSeeAlso({ DateInput.class, StringInput.class, ComplexInput.class })
@JsonSubTypes({ @Type(value = StringInput.class, name = "str"), 
		@Type(value = ComplexInput.class, name = "cplx") })
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "@class")
public abstract class AnalyticsOperationInput extends RequestFactoryEntity implements IAnalyticsOperationInput {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -1911612342867393386L;

	/**
	 * Name of the input variable for the script.
	 */
	private String inputName;

	/**
	 * This is the display name for the input.
	 */
	private String displayName;

	/**
	 * If variable is required to run analytics task.
	 */
	private Boolean required = false;

	public AnalyticsOperationInput() {
	}

	public AnalyticsOperationInput(String inputName) {
		this.inputName = inputName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput
	 * #getInputName ()
	 */
	@Override
	public String getInputName() {
		return inputName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput
	 * #setInputName (java.lang.String)
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput
	 * #isRequired ()
	 */
	public Boolean getRequired() {
		return required;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput
	 * #setRequired (java.lang.Boolean)
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput
	 * #getDisplayName ()
	 */
	public String getDisplayName() {
		return displayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput
	 * #setDisplayName (java.lang.String)
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#
	 * getValueAsString()
	 */
	@Override
	public abstract String getValueAsString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput
	 * #clone()
	 */
	@Override
	public AnalyticsOperationInput clone() {
		AnalyticsOperationInput clone = (AnalyticsOperationInput) super.clone();
		return clone;
	}
}