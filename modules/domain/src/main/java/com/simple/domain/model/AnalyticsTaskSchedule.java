package com.simple.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AnalyticsTaskSchedule {

	@Column(name = "schedule_enabled")
	private boolean enabled;

	@Column(name = "schedule_name")
	private String name;

	@Column(name = "schedule_description")
	private String description;

	@Column(name = "schedule_cronexpression")
	private String cronExpression;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
