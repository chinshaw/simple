package com.simple.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.simple.original.api.analytics.IPerson;

/**
 * Changelog is a class used to store historical changes to entities like the
 * analytics operation and analytics task. This is useful for diagnosing
 * problems and possibly assigning blame when something is changed.
 * 
 * @author chinshaw
 */
@Entity
public class ChangeLog extends DatastoreObject {

	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = -65096843952675994L;

	@Temporal(TemporalType.TIMESTAMP)
	private Date changeDate;

	/**
	 * The person who made the change.
	 */
	@OneToOne(targetEntity = Person.class)
	@NotNull(message = "person is a required field for a changelog")
	private IPerson person;

	@Size(max = 256, message = "change message cannot be larger than 256 characters")
	private String changeMessage;

	/**
	 * This will assign the current authenticating person to the change log.
	 */
	public ChangeLog() {
	}

	/**
	 * Constructor that will assign the person for the change log.
	 * 
	 * @param person
	 */
	public ChangeLog(IPerson person) {
		this(person, null);
	}

	/**
	 * Constructor that is called by all other constructors, it assigns the
	 * person who made the change and adds the changeMessage.
	 * 
	 * @param person
	 *            Person who made the change
	 * @param changeMessage
	 *            The message that other users will be able to use for tracking
	 *            changes to the owning entitiy, max length is 256 characters
	 */
	public ChangeLog(IPerson person, String changeMessage) {
		this.person = person;
		this.changeMessage = changeMessage;
	}

	/**
	 * The person who made the change
	 * 
	 * @return The person who made the last change.
	 */
	public IPerson getPerson() {
		return person;
	}

	/**
	 * This is the person that changed the log this will be mostly automated by
	 * using security.
	 * 
	 * @param person
	 */
	public void setPerson(IPerson person) {
		this.person = person;
	}

	/**
	 * Get the change message associated with this change.
	 * 
	 * @return
	 */
	public String getChangeMessage() {
		return changeMessage;
	}

	/**
	 * Add a change message to the change log to describe what was changed.
	 * 
	 * @param changeMessage
	 */
	public void setChangeMessage(String changeMessage) {
		this.changeMessage = changeMessage;
	}

	/**
	 * This is the date that the change was captured on.
	 * 
	 * @return
	 */
	public Date getChangeDate() {
		return changeDate;
	}

	@PrePersist
	protected void onPrePersist() {
		this.changeDate = new Date();
	}
}
