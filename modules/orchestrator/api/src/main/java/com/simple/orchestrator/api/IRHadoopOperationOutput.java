package com.simple.orchestrator.api;

import javax.xml.bind.annotation.XmlType;

public interface IRHadoopOperationOutput {

	@XmlType(name="operation_type")
	enum Type {
	    AUTO,
	    GRAPHIC,
	    TEXT,
	    NUMERIC,
	    TWO_DIMENSIONAL,
	    BINARY
	}

	Type getOutputType();

	boolean isRequired();
	
	void setRequired(boolean required);

	String getWorkspaceVarName();

	void setWorkspaceVarName(String name);
}
