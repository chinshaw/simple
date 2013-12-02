package com.simple.original.api.analytics;



public interface IPreferences extends IRequestFactoryEntity {

	public Boolean getEmailFlag();

	public String getSubscriberMailId();

	public void setSubscriberMailId(String string);

	public void setDefaultPlace(String token);

	public String getDefaultPlace();

	public String getSubscriberCellNumber();

	public void setSubscriberCellNumber(String subscriberCellNumber);

	public void setEmailFlag(Boolean emailFlag);

	public void setSmsFlag(Boolean smsFlag);

	public Boolean getSmsFlag();

	public void setCellPhoneProvider(String cellPhoneProvider);

	public String getCellPhoneProvider();
	
}
