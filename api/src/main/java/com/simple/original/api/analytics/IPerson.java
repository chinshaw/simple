package com.simple.original.api.analytics;





public interface IPerson extends IRequestFactoryEntity {

	public enum Role {
		ADMIN,
		USER
	}
	
	public abstract Long getId();

	public abstract IPreferences getPreferences();
	
	public abstract String getName();
	
	public abstract String getEmail();
	

}
