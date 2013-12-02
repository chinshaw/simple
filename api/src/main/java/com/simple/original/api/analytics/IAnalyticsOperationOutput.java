package com.simple.original.api.analytics;

import java.util.List;

public interface IAnalyticsOperationOutput extends IRequestFactoryEntity {

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
	
	List<? extends IAnalyticsTaskMonitor> getMonitors();
	
	
}
