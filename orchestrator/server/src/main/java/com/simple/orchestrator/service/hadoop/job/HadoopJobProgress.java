package com.simple.orchestrator.service.hadoop.job;

import java.util.List;

import com.simple.orchestrator.api.IJobProgress;

public class HadoopJobProgress implements IJobProgress {

	
	private int completion;
	
	private List<String> warnings;
	
	private List<String> errors;
	
	
	@Override
	public int getPercentageComplete() {
		return completion;
	}
	
	public void setPercentageComplete(int completion) {
		this.completion = completion;
	}

	@Override
	public List<String> getWarnings() {
		return this.warnings;
	}
	
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	@Override
	public List<String> getErrors() {
		return errors;
	}
	
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
