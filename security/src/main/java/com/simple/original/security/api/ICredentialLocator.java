package com.simple.original.security.api;

public interface ICredentialLocator {
	
	public IHasCredentials find(String userName);
}
