package com.simple.security.api;

import java.util.Set;

import com.simple.api.orchestrator.IPerson.Role;

public interface IHasCredentials {

	public String getUserName();
	
	public String getPassword();

	public Set<Role> getRoles();

	public String getName();
}
