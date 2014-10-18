package com.simple.orchestrator.api.hadoop.operation;

import javax.xml.bind.annotation.XmlRootElement;

import com.simple.orchestrator.api.IRHadoopOperationOutput;

@XmlRootElement
public class ROperationOutput extends AbstractOperationOutput implements IRHadoopOperationOutput {

	private String workspaceVarName;

	private boolean required;

	private Type type;
	
	private Long id;
	
	
	public ROperationOutput() {}
	
	public ROperationOutput(String workspaceVarName, Type type) {
		this.workspaceVarName = workspaceVarName;
		this.type = type;
	}

	public String getWorkspaceVarName() {
		return workspaceVarName;
	}

	public void setWorkspaceVarName(String workspaceVarName) {
		this.workspaceVarName = workspaceVarName;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Type getOutputType() {
		return type;
	}

	public void setOutputType(Type type) {
		this.type = type;
	}
	
	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

}
