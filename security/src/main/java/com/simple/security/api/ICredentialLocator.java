package com.simple.security.api;

public interface ICredentialLocator {
	
	public IHasCredentials find(String userName);
}
