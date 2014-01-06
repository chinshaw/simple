package com.simple.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.simple.original.api.analytics.IAnalyticsTaskMonitor;

/**
 * 
 * A monitor is a specific rule set that monitors the outputs from an analytics
 * task. When the {@link #getAnalyticsTask()} specific task is executed a
 * monitor can watch specific metrics and create alerts based on violations
 * attached to those metrics. This also has a flag for filing a quix case if
 * requested.
 * 
 * @author chinshaw
 */
@Entity
@Access(AccessType.FIELD)
/*
 * @NamedQueries( value = {
 * 
 * @NamedQuery(name = "AnalyticsTaskMonitor.all", query=
 * "SELECT monitor FROM AnalyticsTaskMonitor AS monitor"),
 * 
 * @NamedQuery(name = "AnalyticsTaskMonitor.byTasks", query=
 * "SELECT monitor FROM AnalyticsTaskMonitor AS monitor WHERE monitor.analyticsTask.id IN :taskIdList"
 * ),
 * 
 * @NamedQuery(name = "AnalyticsTaskMonitor.byMetricId", query=
 * "SELECT monitor FROM AnalyticsTaskMonitor AS monitor WHERE monitor.metric.id = :metric"
 * ),
 * 
 * @NamedQuery(name = "AnalyticsTaskMonitor.byMetrics", query=
 * "SELECT monitor FROM AnalyticsTaskMonitor AS monitor WHERE monitor.metric IN :metrics"
 * ) })
 */
public class Monitor extends RequestFactoryEntity implements
		IAnalyticsTaskMonitor {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -2161702960145231775L;

	/**
	 * Is this a public Monitor.
	 */
	@Basic(optional = false)
	private boolean isPublic = false;

	/**
	 * Description of Alert
	 */
	@Basic
	@Size(max = 2048)
	private String description;

	/**
	 * Name of Alert
	 */
	@Basic(optional = false)
	@NotNull(message = "Alert Name is required and must be between 2 and 256 characters")
	@Size(min = 5, max = 256, message = "Alert Name must be between 5 and 256 characters")
	private String name;

	/**
	 * Status of Alert either Active or Inactive
	 */
	@Basic
	@NotNull
	private boolean alertStatus = true;

	/**
	 * Used to set whether or not to create a quix case when the alert is fired
	 */
	@Basic
	private boolean quixEnabled = false;

	/**
	 * Persons subscribed for Alert to receive notifications
	 */
	@OneToMany(orphanRemoval = false, targetEntity = Person.class)
	@JoinTable(name = "analyticstaskmonitor_subscribers", joinColumns = { @JoinColumn(name = "fk_analyticstaskmonitor_id") }, inverseJoinColumns = { @JoinColumn(name = "fk_person_id") })
	private List<Person> subscribers;

	/**
	 * Task assigned for Alert
	 */
	@OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = false, optional = false)
	@NotNull(message = "Monitor requires a valid analtyics task")
	private AnalyticsTask analyticsTask;

	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private AnalyticsOperationOutput output;
	
	/**
	 * owner of the alert
	 */
	@OneToOne(targetEntity = Person.class)
	@NotNull(message = "Owner of the monitor is required")
	private Person owner;

	/**
	 * Factories related to alert
	 */
	@OneToMany(mappedBy = "monitor")
	private List<MonitorAlert> triggeredAlerts;

	/**
	 * Required default constructor
	 */
	public Monitor() {
		subscribers = new ArrayList<Person>();
		triggeredAlerts = new ArrayList<MonitorAlert>();
	}

	/**
	 * Constructor with name parameter, name will be the name used to identify
	 * this monitor.
	 * 
	 * @param name
	 */
	public Monitor(String name) {
		this();
		this.name = name;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * Getter for alertDescription. Description about this alert
	 * 
	 * @return alertDescription Description of the alert
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for alert description
	 * 
	 * @param alertDescription
	 *            Description of the alert
	 */
	public void setDescription(String alertDescription) {
		this.description = alertDescription;
	}

	/**
	 * Name of the alert
	 * 
	 * @return alertName Name of the alert
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for alertName
	 * 
	 * @param alertName
	 */
	public void setName(String alertName) {
		this.name = alertName;
	}

	/**
	 * Getter for alertStatus
	 * 
	 * @return alertStatus Is this alert active?
	 */
	public Boolean getAlertStatus() {
		return alertStatus;
	}

	/**
	 * Setter for alertStatus
	 * 
	 * @param alertStatus
	 *            active status of the alert
	 */
	public void setAlertStatus(Boolean alertStatus) {
		this.alertStatus = alertStatus;
	}

	/**
	 * Getter for subscribers
	 * 
	 * @return subscribers List of users subscribed to the alert
	 */
	public List<Person> getSubscribers() {
		return subscribers;
	}

	/**
	 * Setter for subscribers
	 * 
	 * @param subscribers
	 *            subscribers associated with alert
	 */
	public void setSubscribers(List<Person> subscribers) {
		this.subscribers = subscribers;
	}

	/**
	 * Getter for analyticsTask
	 * 
	 * @return analyticsTask The task to which alert is binded to
	 */
	public AnalyticsTask getAnalyticsTask() {
		return analyticsTask;
	}

	/**
	 * Setter for analyticsTask
	 * 
	 * @param analyticsTask
	 *            The task to which alert is associated with
	 */
	public void setAnalyticsTask(AnalyticsTask analyticsTask) {
		this.analyticsTask = analyticsTask;
	}

	/**
	 * Getter for the output
	 * @return
	 */
	public AnalyticsOperationOutput getOutput() {
		return output;
	}
	
	/**
	 * Setter for the monitored output.
	 * @param output
	 */
	public void setOutput(AnalyticsOperationOutput output) {
		this.output = output;
	}
	
	/**
	 * Sets the owner of the alert.
	 * 
	 * @param owner
	 */
	public void setOwner(Person owner) {
		this.owner = owner;
	}

	/**
	 * Getter for the owner
	 * 
	 * @return Person
	 */
	public Person getOwner() {
		return owner;
	}

	/**
	 * Is firing of quix case enabled.
	 * 
	 * @return
	 */
	public boolean isQuixEnabled() {
		return quixEnabled;
	}

	/**
	 * Will quix case be created when this monitor detects a violation in the
	 * {@link #getMetric()} output.
	 * 
	 * @param enableQuix
	 */
	public void setQuixEnabled(boolean enableQuix) {
		this.quixEnabled = enableQuix;
	}

	/**
	 * Getter for factoryAlert
	 * 
	 * @return factoryAlert List of factoryAlerts related to the alert
	 */
	public List<MonitorAlert> getFactoryAlert() {
		return triggeredAlerts;
	}

	/**
	 * Setter for factoryAlert
	 * 
	 * @param factoryAlert
	 *            factoryAlerts associated with alert
	 */
	public void setFactoryAlert(List<MonitorAlert> factoryAlert) {
		this.triggeredAlerts = factoryAlert;
	}

}