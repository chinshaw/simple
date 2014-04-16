package com.simple.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import com.simple.api.orchestrator.IAnalyticsTask;
import com.simple.api.orchestrator.IPerson;
import com.simple.domain.model.dataprovider.DataProvider;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.dashboard.Dashboard;

/**
 * This is an analtyics task template used to describe the execution task chain
 * for the Analtyics task objects.
 * 
 * @author chinshaw
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "analyticstask", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"task_name", "owner_id" }) })
@NamedQueries(value = {
		@NamedQuery(name = "AnalyticsTask.all", query = "select task from AnalyticsTask as task order by task.name"),
		@NamedQuery(name = "AnalyticsTask.byName", query = "select task from AnalyticsTask as task where task.name in :name order by task.name"),
		@NamedQuery(name = "AnalyticsTask.byOwner", query = "select task from AnalyticsTask as task where task.owner.id = :owner order by task.name"),
		@NamedQuery(name = "AnalyticsTask.countByOwner", query = "select count(task) from AnalyticsTask as task where task.owner.id= :userId and task.isPublic = false") })
public class AnalyticsTask extends RequestFactoryEntity implements
		IAnalyticsTask {

	/**
	 * Serialization id.
	 */
	private static final long serialVersionUID = -1515828550275481247L;

	/**
	 * This is an option that was put in to make the analytics task public to
	 * all users. This is probably more for future use. The reason the Column
	 * annotation was added is because it will result in a table name of public
	 * which breaks ms sql.
	 */
	@Column(name = "isPublic")
	private boolean isPublic;

	/**
	 * Name of the analytics task/Report.
	 */
	@Size(min = 3, max = 256, message = "Name must be between 3 and 256 characters")
	@NotNull(message = "Name is required and must be between 3 and 256 characters")
	@Column(name = "task_name")
	private String name;

	/**
	 * The description of the task.
	 */
	@Size(max = 2048, message = "Description cannot exceed 2048 characters")
	@Column(name = "task_description")
	private String description;

	/**
	 * This is the person who created the task originally
	 */
	@OneToOne(targetEntity = Person.class)
	@JoinColumn(name = "owner_id")
	private Person owner;

	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "analyticsTask")
	private List<Dashboard> dashboards = new ArrayList<Dashboard>();

	/**
	 * List of subscribers for the task
	 */
	@OneToMany(targetEntity = Person.class)
	private List<Person> subscribers = new ArrayList<Person>();

	/**
	 * This is the unique fingerprint for the task, it will always be unique and
	 * can be used to merging tasks.
	 */
	@Column(name = "fingerprint")
	private String fingerprint;

	/**
	 * List of changes made this task
	 * 
	 * @see ChangeLog
	 */
	/**
	 * List of changes made this operation
	 * 
	 * @see ChangeLog
	 */
	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "analyticstask_changelogs", joinColumns = { @JoinColumn(name = "fk_analyticstask_id") }, inverseJoinColumns = { @JoinColumn(name = "fk_changelog_id") })
	private List<ChangeLog> changeLogs = new ArrayList<ChangeLog>();

	/**
	 * The embedded schedule for this task.
	 */
	@Embedded
	private AnalyticsTaskSchedule schedule;

	/**
	 * List of inputs to provide the the operations. This will typically
	 * override the operation default inputs.
	 */
	@OneToMany
	private List<AnalyticsOperationInput> taskInputs;

	/**
	 * One to one relationship for the script that contains the actual data.
	 * Script may make it's way into this class later on.
	 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.DETACH })
	// @JoinTable(name = "analyticstask_analyticsoperations", joinColumns = {
	// @JoinColumn(name = "fk_analyticstask_id") }, inverseJoinColumns = {
	// @JoinColumn(name = "fk_analyticsoperations_id") })
	@OrderColumn(name = "analyticsoperations_order")
	@Size(min = 1, message = "Atleast one operation is required for the task")
	private List<AnalyticsOperation> operations;

	@OneToMany
	private List<DataProvider> dataProviders;

	/**
	 * Required default constructor.
	 */
	public AnalyticsTask() {
		this(null);
	}

	public AnalyticsTask(String name) {
		this.name = name;
		dataProviders = new ArrayList<DataProvider>();
		operations = new ArrayList<AnalyticsOperation>();
		fingerprint = UUID.randomUUID().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.server.domain.IAnalyticsTask#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.simple.original.server.domain.IAnalyticsTask#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.simple.original.server.domain.IAnalyticsTask#getAnalyticsOperations()
	 */
	public List<AnalyticsOperation> getAnalyticsOperations() {
		return operations;
	}

	public void setAnalyticsOperations(
			List<AnalyticsOperation> analyticsOperations) {
		this.operations = analyticsOperations;
	}

	@XmlTransient
	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	@Override
	public List<AnalyticsOperation> getOperations() {
		return operations;
	}

	/**
	 * Gets all the dashboards for this task, it is a one to many so there could
	 * be multiples.
	 * 
	 * @return
	 */
	public List<Dashboard> getDashboards() {
		return dashboards;
	}

	/**
	 * Get the dashboard for this task, this returns the first one in the list
	 * which is set as teh default dashboard.
	 * 
	 * @return
	 */
	public Dashboard getDashboard() {
		if (dashboards != null && !dashboards.isEmpty()) {
			return dashboards.get(0);
		}

		return null;
	}

	/**
	 * 
	 * @param dashboard
	 */
	public void setDashboard(Dashboard dashboard) {
		if (dashboards.contains(dashboard)) {
			Collections.swap(dashboards, dashboards.indexOf(dashboard), 0);
		} else {
			addDashboard(dashboard, true);
		}
	}

	/**
	 * Set the dashboard for this task
	 * 
	 * @param dashboard
	 */
	public void addDashboard(Dashboard dashboard) {
		if (dashboard == null) {
			throw new IllegalArgumentException("dashboard cannot be null");
		}

		dashboards.add(dashboard);
	}

	public void addDashboard(Dashboard dashboard, boolean isDefault) {
		if (dashboard == null) {
			throw new IllegalArgumentException("dashboard cannot be null");
		}

		if (isDefault) {
			dashboards.add(0, dashboard);
		} else {
			dashboards.add(dashboard);
		}
	}

	/**
	 * 
	 * @see #getTaskInputs()
	 * @return
	 */
	public List<AnalyticsOperationOutput> getAllOutputs() {
		List<AnalyticsOperationOutput> allOutputs = new ArrayList<AnalyticsOperationOutput>();
		for (AnalyticsOperation operation : getOperations()) {
			allOutputs.addAll(operation.getOutputs());
		}
		return allOutputs;
	}

	/**
	 * 
	 * @see #setTaskInputs(List)
	 * @return
	 */
	public List<AnalyticsOperationInput> getAllInputs() {
		List<AnalyticsOperationInput> allInputs = new ArrayList<AnalyticsOperationInput>();
		for (AnalyticsOperation operation : getOperations()) {
			allInputs.addAll(operation.getInputs());
		}
		return allInputs;
	}

	public List<AnalyticsOperationInput> getTaskInputs() {
		return taskInputs;
	}

	public void setTaskInputs(List<AnalyticsOperationInput> taskInputs) {
		this.taskInputs = taskInputs;
	}

	@Override
	public List<DataProvider> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProvider> dataProviders) {
		this.dataProviders = dataProviders;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public boolean isScheduleEnabled() {
		if (schedule == null) {
			return false;
		}
		return schedule.isEnabled();
	}

	public void setScheduleEnabled(boolean isEnabled) {
		if (schedule == null) {
			throw new RuntimeException(
					"Can't set schedule enabled because there isn't one created");
		}
		this.schedule.setEnabled(isEnabled);
	}

	public AnalyticsTaskSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(AnalyticsTaskSchedule schedule) {
		this.schedule = schedule;
	}

	/**
	 * Retrieve the last log in the change logs for this task.
	 * 
	 * @return
	 */
	public ChangeLog getLastChangeLog() {
		if (changeLogs == null || changeLogs.size() == 0) {
			return null;
		}
		return changeLogs.get(changeLogs.size() - 1);
	}

	/**
	 * Fetches all the change logs for this task, since the beginning of time.
	 * 
	 * @return
	 */
	public List<ChangeLog> getChangeLogs() {
		return changeLogs;
	}

	public void addChangeLog(ChangeLog changeLog) {
		if (changeLogs == null) {
			changeLogs = new ArrayList<ChangeLog>();
		}
		changeLogs.add(changeLog);
	}

	/**
	 * The date that the operation was last modified.
	 * 
	 * @return The last modified date or the null if there were no
	 *         modifications.
	 */
	public Date getModifiedDate() {
		ChangeLog last = getLastChangeLog();
		if (last == null) {
			return null;
		}
		return last.getChangeDate();
	}

	/**
	 * The last person to make modifications to this task.
	 * 
	 * @return The last person to modify the operation or null if there are no
	 *         modifications.
	 */
	public IPerson getLastModifiedBy() {
		ChangeLog last = getLastChangeLog();
		if (last == null) {
			return null;
		}
		return last.getPerson();
	}

	public AnalyticsTask clone() {
		AnalyticsTask clone = (AnalyticsTask) super.clone();

		clone.setOwner(null);
		// Set the list of operations no need for clone.
		clone.setAnalyticsOperations(getAnalyticsOperations());

		clone.changeLogs = new ArrayList<ChangeLog>();

		clone.dashboards = new ArrayList<Dashboard>();

		Dashboard clonedDashboard = getDashboard().clone();
		clonedDashboard.setAnalyticsTask(clone);

		clone.setDashboard(clonedDashboard);
		return clone;
	}

	public void addOperation(RAnalyticsOperation operation) {
		operations.add(operation);
	}

	public void addOperation(int index, AnalyticsOperation operation) {
		operations.add(operation);
	}

	/**
	 * Get the list of people subscribed to this task.
	 * 
	 * @return
	 */
	public List<Person> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<Person> subscribers) {
		this.subscribers = subscribers;
	}

	public void addSubscriber(Person subscriber) {
		if (subscribers.contains(subscriber)) {
			throw new IllegalArgumentException("Subscriber already added");
		}
		subscribers.add(subscriber);
	}

}
