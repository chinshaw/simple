package com.simple.orchestrator.api.hadoop.operation;

import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ROperation extends Operation {
	
	private String code;

	private String jobName;
	
	private Long operationId;
	
	private Collection<ROperationOutput> outputs = new HashSet<ROperationOutput>();
	
	/**
	 * Required default constructor.
	 */
	public ROperation() {
	}
	
	public ROperation(String jobName) {
		this.jobName = jobName;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getId() {
		return operationId;
	}
	
	public void setId(Long operationId) {
		this.operationId = operationId;
	}

	public Collection<ROperationOutput> getOutputs() {
		return outputs;
	}
	
	public void setOutputs(Collection<ROperationOutput> outputs) {
		this.outputs = outputs;
	}
	
	public void addOutput(ROperationOutput output) {
		outputs.add(output);
	}
}
