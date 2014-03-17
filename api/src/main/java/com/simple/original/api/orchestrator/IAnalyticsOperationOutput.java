package com.simple.original.api.orchestrator;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

public interface IAnalyticsOperationOutput extends IRequestFactoryEntity {

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

	String getName();

	void setName(String name);
}
