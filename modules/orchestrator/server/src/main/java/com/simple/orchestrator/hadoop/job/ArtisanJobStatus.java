package com.simple.orchestrator.hadoop.job;

import org.apache.hadoop.mapreduce.JobStatus;

import com.google.common.eventbus.EventBus;

public class ArtisanJobStatus extends JobStatus {

	private EventBus eventBus;
	
	public ArtisanJobStatus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}
