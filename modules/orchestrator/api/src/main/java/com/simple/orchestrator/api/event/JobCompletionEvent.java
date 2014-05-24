package com.simple.orchestrator.api.event;

/**
 * Job completion event is fired when a job completes.
 * This contains the unique hadoop jobid and the
 * job status that comes from hadoop. 
 * 
 * @author chris
 */
public class JobCompletionEvent {

	private String jobId;

	private String jobStatus;

	/**
	 * Empty constructor
	 */
	public JobCompletionEvent() {

	}

	/**
	 * @param jobId
	 * @param jobStatus
	 */
	public JobCompletionEvent(String jobId, String jobStatus) {
		super();
		this.jobId = jobId;
		this.jobStatus = jobStatus;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
		result = prime * result + ((jobStatus == null) ? 0 : jobStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobCompletionEvent other = (JobCompletionEvent) obj;
		if (jobId == null) {
			if (other.jobId != null)
				return false;
		} else if (!jobId.equals(other.jobId))
			return false;
		if (jobStatus == null) {
			if (other.jobStatus != null)
				return false;
		} else if (!jobStatus.equals(other.jobStatus))
			return false;
		return true;
	}
}
