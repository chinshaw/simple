package com.simple.orchestrator.api;

import java.util.List;

public interface IJobProgress {

	public int getPercentageComplete();

	public List<String> getWarnings();

	public List<String> getErrors();
	
}
