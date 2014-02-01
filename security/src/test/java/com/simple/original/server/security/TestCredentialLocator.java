package com.simple.original.server.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.simple.original.api.analytics.IPerson.Role;
import com.simple.original.security.api.ICredentialLocator;
import com.simple.original.security.api.IHasCredentials;

public class TestCredentialLocator implements ICredentialLocator {

	private Map<String, IHasCredentials> users = new HashMap<String, IHasCredentials>();
	
	
	public TestCredentialLocator() {
		users.put("testuser@test.com", new IHasCredentials() {
			
			@Override
			public String getUserName() {
				return "testuser@test.com";
			}
			
			@Override
			public Set<Role> getRoles() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPassword() {
				return "password";
			}
			
			@Override
			public String getName() {
				return "Test User";
			}
		});
	}
	
	
	@Override
	public IHasCredentials find(String username) {
		return users.get(username);
	}

}
