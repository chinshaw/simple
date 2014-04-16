package com.simple.domain.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import com.simple.api.orchestrator.IAnalyticsOperationOutput;

/**
 * This class is an output from the operation, in previous versions the
 * operation had a metric object without a value to fetch. This will deprecate
 * that api and the database will have to be migrated in order to make this
 * possible but it is necessary and will alleviate confusion.
 * 
 * @author chinshaw
 */
@Entity
@Access(AccessType.FIELD)
public class AnalyticsOperationOutput extends RequestFactoryEntity implements IAnalyticsOperationOutput {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -289683588735461360L;

	/**
	 * This is the type of output, this is used when the user tries to render
	 * the output into a widget so that we can give the correct types when
	 * listing available outputs.
	 */
	private IAnalyticsOperationOutput.Type outputType;

	/**
	 * This is the variable name used to identify the output from the operation.
	 */
	@Basic(optional = false)
	private String name;

	@Basic
	@Size(max = 255)
	private String description;

	/**
	 * If this output is required or not, can be identified in the ui as a
	 * required output.
	 */
	@Basic(optional = false)
	private boolean required = false;

	/**
	 * This is the operation that owns this output.
	 */
	// @ManyToOne
	// @JoinColumn(name = "operation_id")
	// private AnalyticsOperation operation;

	/**
	 * Empty constructor.
	 */
	public AnalyticsOperationOutput() {
		this(null);
	}

	/**
	 * Constructor that will initialize the name of the output, this is also
	 * used for the variable name.
	 * 
	 * @param name
	 */
	public AnalyticsOperationOutput(String name) {
		this(name, null);
	}

	/**
	 * Constructor that takes the name of the operation along with it's type.
	 * 
	 * @param name
	 *            The name of the output.
	 * @param outputType
	 *            The output type.
	 */
	public AnalyticsOperationOutput(String name,
			IAnalyticsOperationOutput.Type outputType) {
		this.name = name;
		this.outputType = outputType;
	}

	/**
	 * The name of the output which will be used when retriving the output from
	 * the workspace.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the output, this is used when retrieving the output from
	 * the operation after it has completed succesfully.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Is the output required by the operation?
	 * 
	 * @return boolean if it's required or not
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Set whether the output required by the operation.
	 * 
	 * @param required
	 *            set whether this output is required when fetching outputs
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * User defined output type for describing the possible output type. We will
	 * try to honor this type when storing the output but not guaranteed.
	 * 
	 * @return
	 */
	public IAnalyticsOperationOutput.Type getOutputType() {
		return outputType;
	}

	/**
	 * Output type is a user defined output type that is useful for describing
	 * to the dashboard what it can render.
	 * 
	 * @param outputType
	 */
	public void setOutputType(IAnalyticsOperationOutput.Type outputType) {
		this.outputType = outputType;
	}

	/**
	 * User defined description of the output, this is optional but is useful
	 * for describing the value in which the user is seeing.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the optional description of the output.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	/**
	 * Clone will clone the entire operation this will call the
	 * {@link DatastoreObject} class's clone method which will reset the id to
	 * null and the version back to 0 if it was not already.
	 */
	public AnalyticsOperationOutput clone() {
		return (AnalyticsOperationOutput) super.clone();
	}

}