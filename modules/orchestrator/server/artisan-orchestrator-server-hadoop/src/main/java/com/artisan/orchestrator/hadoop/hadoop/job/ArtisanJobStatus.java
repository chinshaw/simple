package com.artisan.orchestrator.hadoop.hadoop.job;

import org.apache.hadoop.mapreduce.JobStatus;

import com.google.common.eventbus.EventBus;

public class ArtisanJobStatus extends JobStatus {

	public static class JobStatusChangedEvent {
		
		private State state;
		
		private String jobIdentifier;
		
		public JobStatusChangedEvent(String jobIdentifider, State state) {
			this.jobIdentifier = jobIdentifider;
			this.state = state;
		}
		
		public State getState() {
			return state;
		}
		
		public String getJobIdentifer() {
			return jobIdentifier;
		}
	}
	
	
	private EventBus eventBus;
	
	public ArtisanJobStatus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	  protected synchronized void setState(State state) {
		  System.out.println("SSSSSSSEEEEEEEEEEEEEEEEEETTING STATE");
		  eventBus.post(new JobStatusChangedEvent(getJobID().getJtIdentifier(), state));
		  super.setState(state);
	  }
}
