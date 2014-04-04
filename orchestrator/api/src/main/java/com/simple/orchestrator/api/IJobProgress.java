package com.simple.orchestrator.api;

import java.io.IOException;


public interface IJobProgress {

	public float getPercentageComplete();

	public String getErrors();
}
