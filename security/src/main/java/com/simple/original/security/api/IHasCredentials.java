package com.simple.original.security.api;

import java.util.Set;

import com.simple.original.api.orchestrator.IPerson.Role;

public interface IHasCredentials {

	public String getUserName();
	
	public String getPassword();

	public Set<Role> getRoles();

	public String getName();
}
