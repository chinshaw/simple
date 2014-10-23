package com.simple.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.DataProviderInput;

/**
 * 
 * @author chinshaw
 */
@Entity
@XmlRootElement
@XmlSeeAlso({RAnalyticsOperation.class})
@Inheritance(strategy =InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "@class")
@JsonSubTypes({ @JsonSubTypes.Type(value = RAnalyticsOperation.class, name="r")})
@JsonIgnoreProperties(value = {"lastChangeLog", "modifiedDate", "lastModifiedBy"})
public abstract class AnalyticsOperation extends RequestFactoryEntity   {

	/**
	 * Serialization Id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is an option that was put in to make the analytics operation public
	 * to all users.
	 */
	@Column(name = "isPublic")
	private boolean publicAccessible;

	/**
	 * Name of the operation.
	 */
	@NotNull(message = "Name is required and must be between 3 and 256 characters")
	@Size(min = 3, max = 256, message = "Name must be between 3 and 256 characters")
	@Column(name = "name")
	protected String name;

	/**
	 * Text description of the operation.
	 */
	@Size(max = 2048, message = "Descritption must be less than 2048 characters")
	@Column(name = "description")
	protected String description;

	/**
	 * owner of operation
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "owner_id")
	private Person owner;

	/**
	 * This is the data provider for this analytics task. This could be dynamic
	 * and the script never change.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<DataProviderInput> dataProviders = new ArrayList<DataProviderInput>();

	
	/**
	 * The list of analytics operation inputs, since this is an abstract class
	 * we can't use the mapped by clause.
	 */
	@OrderColumn(name = "inputs_order")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="operation_id", insertable=false, updatable=false)
	//@JoinTable(name = "analyticsoperation_inputs", joinColumns = { @JoinColumn(name = "fk_analyticsoperation_id") }, inverseJoinColumns = { @JoinColumn(name = "fk_analyticsoperation_input_id") })
	private List<AnalyticsOperationInput> inputs = new ArrayList<AnalyticsOperationInput>();

	/**
	 * The list of analytics operation outputs, these define whether they are
	 * required or not and will be evaluated when the operation completes it's
	 * execution cycle.
	 */
	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, targetEntity = AnalyticsOperationOutput.class)
	private List<AnalyticsOperationOutput> outputs = new ArrayList<AnalyticsOperationOutput>();

	/**
	 * List of changes made this operation
	 * 
	 * @see ChangeLog
	 */
	@JsonIgnore
	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChangeLog> changeLogs = new ArrayList<ChangeLog>();

	/**
	 * Simple constructor initalizes an empty inputs and outputs array.
	 */
	public AnalyticsOperation() {
	}

	/**
	 * Get the name of the analytics operation.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the analtyics operation.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the description of the analytics operation.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the description of this analytics operation.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Set analytics task inputs.
	 * 
	 * @param inputs
	 */
	public void setInputs(List<AnalyticsOperationInput> inputs) {
		this.inputs = inputs;
	}

	/**
	 * Get analytics task inputs.
	 * 
	 * @return
	 */
	public List<AnalyticsOperationInput> getInputs() {
		return inputs;
	}

	/**
	 * Used to add a single input to the list of inputs.
	 */
	public void addInput(AnalyticsOperationInput input) {
		inputs.add(input);
	}
	
	/**
	 * Used to add a single input at a specific index
	 * @param index
	 * @param input
	 */
	public void addInput(int index, AnalyticsOperationInput input) {
		inputs.add(index, input);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.server.domain.IAnalyticsTask#getDataProviders()
	 */
	public List<DataProviderInput> getDataProviders() {
		return this.dataProviders;
	}

	public void setDataProviders(List<DataProviderInput> dataProviders) {
		this.dataProviders = dataProviders;
	}
	
	/**
	 * Setter for the outputs.
	 * 
	 * @param outputs
	 */
	public void setOutputs(List<AnalyticsOperationOutput> outputs) {
		this.outputs = outputs;
	}
	
	
	

	/**
	 * Getter for a list of outputs.
	 * 
	 * @return List of outputs to render.
	 */
	public List<AnalyticsOperationOutput> getOutputs() {
		return outputs;
	}

	public void addOutput(AnalyticsOperationOutput output) {
		if (output == null) {
			throw new IllegalArgumentException(
					"AnalyticsOperationOutput cannot be null");
		}
		this.outputs.add(output);
	}

	/**
	 * This is a quirky work around for the problem where revolution does not
	 * provide a script id for their stuff. If the revolution code goes away
	 * remove this method call and change the Datastore object back to private.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the isPublic
	 */
	public boolean getPublicAccessible() {
		return publicAccessible;
	}

	/**
	 * @param isPublic
	 *            the isPublic to set
	 */
	public void setPublicAccessible(boolean publicAccessible) {
		this.publicAccessible = publicAccessible;
	}

	/**
	 * @return the owner
	 */
	@XmlTransient
	public Person getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(Person owner) {
		this.owner = owner;
	}

	/**
	 * Retrieve the last log in the change logs for this task.
	 * 
	 * @return
	 */
	public ChangeLog getLastChangeLog() {
		if (changeLogs == null || changeLogs.size() == 0) {
			return null;
		}
		return changeLogs.get(changeLogs.size() - 1);
	}

	/**
	 * Fetches all the change logs for this task, since the beginning of time.
	 * 
	 * @return
	 */
	@JsonIgnore
	public List<ChangeLog> getChangeLogs() {
		return changeLogs;
	}

	/**
	 * The date that the operation was last modified.
	 * 
	 * @return The last modified date or the null if there were no
	 *         modifications.
	 */
	public Date getModifiedDate() {
		ChangeLog last = getLastChangeLog();
		if (last == null) {
			return null;
		}
		return last.getChangeDate();
	}

	/**
	 * The last person to make modifications to this task.
	 * 
	 * @return The last person to modify the operation or null if there are no
	 *         modifications.
	 */
	public Person getLastModifiedBy() {
		ChangeLog last = getLastChangeLog();
		if (last == null) {
			return null;
		}
		return last.getPerson();
	}

	@PrePersist
	protected void onUpdate() {
		if (owner == null) {
			throw new RuntimeException("analytics operation must have an owner");
		}

		ChangeLog changeLog = new ChangeLog(owner);
		changeLogs.add(changeLog);
	}

	public AnalyticsOperation clone() {
		AnalyticsOperation clone = (AnalyticsOperation) super.clone();
		clone.id = null;
		clone.version = 0;

		clone.setInputs(new ArrayList<AnalyticsOperationInput>());
		for (AnalyticsOperationInput input : inputs) {
			clone.inputs.add(input.clone());
		}

		clone.setOutputs(new ArrayList<AnalyticsOperationOutput>());
		for (AnalyticsOperationOutput output : outputs) {
			clone.outputs.add(output.clone());
		}

		return clone;
	}
}
