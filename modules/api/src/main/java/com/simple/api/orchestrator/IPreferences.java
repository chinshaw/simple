package com.simple.api.orchestrator;



public interface IPreferences extends IRequestFactoryEntity {

	public boolean getEmailFlag();

	public String getSubscriberMailId();

	public void setSubscriberMailId(String string);

	public void setDefaultPlace(String token);

	public String getDefaultPlace();

	public String getSubscriberCellNumber();

	public void setSubscriberCellNumber(String subscriberCellNumber);

	public boolean getSmsFlag();

	public void setCellPhoneProvider(String cellPhoneProvider);

	public String getCellPhoneProvider();
	
}
