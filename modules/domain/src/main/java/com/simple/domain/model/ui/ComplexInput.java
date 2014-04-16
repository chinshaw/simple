package com.simple.domain.model.ui;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.simple.api.orchestrator.IComplexInput;

/**
 * Purpose of this class is to create a dynamic user input model. This is used
 * to model a more complex input object. An example of this would be a Phase
 * which is a time based period that can be anottated for a chart. This is a
 * complex object that has a starting date, ending date and a string annotation.
 * 
 * @author chinshaw
 * 
 */
@Entity
@Table(name = "complex_input")
public class ComplexInput extends AnalyticsOperationInput implements IComplexInput {

	/**
	 * Serialization Id.
	 */
	private static final long serialVersionUID = -6780223499357845745L;

	@OneToMany(cascade = CascadeType.ALL)
	private List<AnalyticsOperationInput> inputs = new ArrayList<AnalyticsOperationInput>();

	/**
	 * Default constructor
	 */
	public ComplexInput() {
		this(null);
	}

	/**
	 * Constructor that will initialize the display name.
	 * 
	 * @param displayName
	 */
	public ComplexInput(String displayName) {
		this(displayName, null);
	}

	/**
	 * Constructor that will initialize the display name and the list of inputs.
	 * 
	 * @param displayName
	 * @param value
	 */
	public ComplexInput(String displayName, List<AnalyticsOperationInput> value) {
		setDisplayName(displayName);
		this.inputs = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.server.domain.IComplexInput#getValueAsString()
	 */
	@Override
	public String getValueAsString() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.server.domain.IComplexInput#getInputs()
	 */
	@Override
	public List<AnalyticsOperationInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<AnalyticsOperationInput> inputs) {
		this.inputs = inputs;
	}
}