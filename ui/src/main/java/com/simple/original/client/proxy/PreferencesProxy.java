package com.simple.original.client.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.model.Preferences;

/**
 * Preferences are the user defined environment variable 
 * given for a specific user, this includes home page, book marks
 * email and sms variables.
 * 
 * @author chinshaw
 *
 */
@ProxyFor(value = Preferences.class)
public interface PreferencesProxy extends ValueProxy {

	public static String[] BOOKMARK_PROPERTIES = { "bookmarks" };

	public void setDefaultPlace(String token);

	public String getDefaultPlace();

	public String getSubscriberCellNumber();

	public void setSubscriberCellNumber(String subscriberCellNumber);

	public String getSubscriberMailId();

	public void setSubscriberMailId(String subscriberMail);

	public boolean getEmailFlag();

	public void setEmailFlag(boolean emailFlag);

	public void setSmsFlag(boolean smsFlag);

	public boolean getSmsFlag();

	public void setCellPhoneProvider(String cellPhoneProvider);

	public String getCellPhoneProvider();

	/**
	 * Getter for the list of the user defined bookarks
	 * @return
	 */
	public List<ApplicationBookmarkProxy> getBookmarks();

}
