package com.simple.domain.model.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.simple.original.api.orchestrator.IDateInput;

@Entity
@Access(AccessType.FIELD)
@Table(name = "date_input")
public class DateInput extends AnalyticsOperationInput implements IDateInput {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = -6100170588580739520L;

    /**
     * This is the default value.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_value")
    private Date value = null;

    public DateInput() {
    	this(null);
    }
    
    public DateInput(String name) {
    	this(name, null);
    }
    
    public DateInput(String inputName, Date value) {
    	super(inputName);
    	this.value = value;
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.server.domain.IDateInput#getDefaultValue()
     */
    @Override
    public Date getValue() {
        return this.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.server.domain.IDateInput#setDefaultValue(java.util.Date)
     */
    public void setValue(Date value) {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.server.domain.IDateInput#getValueAsString()
     */
    @Override
    public String getValueAsString() {
    	if (value != null) {
    		SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
    		return formatter.format(value).toString();
    	}
    	return "";
    }

    public DateInput clone() {
        DateInput clone = (DateInput) super.clone();
        return clone;
    }
}