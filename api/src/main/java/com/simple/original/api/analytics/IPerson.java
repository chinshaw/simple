package com.simple.original.api.analytics;

import java.util.Set;

import com.simple.original.api.analytics.IPerson.Role;





public interface IPerson extends IRequestFactoryEntity {

	public enum Role {
		ADMIN,
		USER
	}
	
	public abstract Long getId();

	public abstract IPreferences getPreferences();
	
	public abstract String getName();
	
	public abstract String getEmail();

	public abstract Set<Role> getRoles();

	public abstract String getPassword();
	

}
