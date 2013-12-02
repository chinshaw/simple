package com.simple.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;

import com.simple.original.api.analytics.IApplicationBookmark;
import com.simple.original.api.analytics.IPreferences;

@Entity
public class Preferences extends RequestFactoryEntity implements IPreferences {

	private static final Logger logger = Logger.getLogger(Preferences.class.getName());
	
	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 6606835622841823855L;
	
	@Basic
	private Boolean debugEnabled;

	@OneToMany(cascade= CascadeType.ALL, targetEntity = ApplicationBookmark.class)
	private List<ApplicationBookmark> bookMarks;

	/**
	 * default place for user to navigate to that page on login
	 */
	private String defaultPlace;

	/**
	 * CellPhone Number for which user wants to subscribe when pattern is
	 * changed here please do modifications in
	 * LDAPQuery.java->getMobileNumber(email)
	 */
	@Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
	private String subscriberCellNumber;

	/**
	 * Email id of for which user wants to subscribe
	 */

	private String subscriberMailId;

	/**
	 * If Email Flag value is true then user subscribed to receive Email
	 * notifications
	 */
	private boolean emailFlag;

	/**
	 * If Sms Flag value is true then user subscribed to receive SMS
	 * notifications
	 */
	private boolean smsFlag;

	/**
	 * cellPhoneProvider value should be as name element value in
	 * cellPhoneProviders.xml
	 */
	private String cellPhoneProvider;

	public Preferences() {
	}

	public Boolean getDebugEnabled() {
		return debugEnabled;
	}

	public void setDebugEnabled(Boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	public void setDefaultPlace(String defaultPlace) {
		this.defaultPlace = defaultPlace;
	}

	public String getDefaultPlace() {
		return defaultPlace;
	}

	public String getSubscriberCellNumber() {
		return subscriberCellNumber;
	}

	public void setSubscriberCellNumber(String subscriberCellNumber) {
		this.subscriberCellNumber = subscriberCellNumber;
	}

	public String getSubscriberMailId() {
		return subscriberMailId;
	}

	public void setSubscriberMailId(String subscriberMailId) {
		this.subscriberMailId = subscriberMailId;
	}

	public String getCellPhoneProvider() {
		return cellPhoneProvider;
	}

	public void setCellPhoneProvider(String cellPhoneProvider) {
		this.cellPhoneProvider = cellPhoneProvider;
	}
	
	public Boolean getEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(Boolean emailFlag) {
		this.emailFlag = emailFlag;
	}

	public Boolean getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(Boolean smsFlag) {
		this.smsFlag = smsFlag;
	}

	public List<ApplicationBookmark> getBookmarks() {
		return bookMarks;
	}

	public boolean addBookmark(ApplicationBookmark bookmark) {
		if (bookmark == null) {
			return false;
		}
		
		if (bookMarks == null) {
			bookMarks = new ArrayList<ApplicationBookmark>();
		}
		
		return bookMarks.add(bookmark);
	}

	public boolean removeBookmark(IApplicationBookmark bookmark) {
		if (bookmark == null) {
			logger.info("Trying to delete a null bookmark");
			return false;
		}
		
		return bookMarks.remove(bookmark);
	}
}