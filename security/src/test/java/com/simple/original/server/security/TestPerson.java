package com.simple.original.server.security;

import java.util.Set;

import com.simple.original.api.orchestrator.IPerson;
import com.simple.original.api.orchestrator.IPreferences;

public class TestPerson implements IPerson {

	private String email;
	
	private String name;
	
	private String password;
	
	private Set<Role> roles;
	
	private IPreferences preferences;
	
	@Override
	public Integer getVersion() {
		return 0;
	}

	@Override
	public void setVersion(Integer version) {
	}

	@Override
	public Long getId() {
		return 0L;
	}

	@Override
	public IPreferences getPreferences() {
		return preferences;
	}

	@Override
	public String getName() {
		return "Test User";
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public Set<Role> getRoles() {
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
		
	}

	public void setPassword(String password) {
		this.password = password;
		
	}

	public void setName(String name) {
		this.name = name;
	}

}
