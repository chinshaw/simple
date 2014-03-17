package com.simple.domain.model.ui;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlRootElement;

import com.simple.domain.model.RequestFactoryEntity;
import com.simple.original.api.orchestrator.IAnalyticsOperationInput;


@Entity
@XmlRootElement
@Access(AccessType.FIELD)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
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
     * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#getInputName
     * ()
     */
    @Override
    public String getInputName() {
        return inputName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#setInputName
     * (java.lang.String)
     */
    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#isRequired
     * ()
     */
    public Boolean getRequired() {
        return required;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#setRequired
     * (java.lang.Boolean)
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#getDisplayName
     * ()
     */
    public String getDisplayName() {
        return displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#setDisplayName
     * (java.lang.String)
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#
     * getValueAsString()
     */
    @Override
    public abstract String getValueAsString();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.taskengine.shared.analytics.IAnalyticsOperationInput#clone()
     */
    @Override
    public AnalyticsOperationInput clone() throws CloneNotSupportedException {
        AnalyticsOperationInput clone = (AnalyticsOperationInput) super.clone();
        return clone;
    }
}