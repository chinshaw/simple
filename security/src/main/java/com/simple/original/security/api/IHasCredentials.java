package com.simple.original.security.api;

import java.util.Set;

import com.simple.original.api.analytics.IPerson.Role;

public interface IHasCredentials {

	public String getUserName();
	
	public String getPassword();

	public Set<Role> getRoles();

	public String getName();
}
