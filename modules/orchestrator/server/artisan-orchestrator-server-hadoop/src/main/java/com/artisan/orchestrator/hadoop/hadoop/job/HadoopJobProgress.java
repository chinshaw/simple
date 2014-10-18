package com.artisan.orchestrator.hadoop.hadoop.job;

import com.simple.orchestrator.api.IJobProgress;

public class HadoopJobProgress implements IJobProgress {

	
	private float completion;
	
	private String errors;
	
	
	@Override
	public float getPercentageComplete() {
		return completion;
	}
	
	public void setPercentageComplete(int completion) {
		this.completion = completion;
	}

	@Override
	public String getErrors() {
		return errors;
	}
	
	public void setErrors(String errors) {
		this.errors = errors;
	}

}
