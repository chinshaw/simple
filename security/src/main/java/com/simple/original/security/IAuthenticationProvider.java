package com.simple.original.security;

import com.simple.domain.Person;

public interface IAuthenticationProvider {

	
	public Person doAuthenticate(String username, String password) throws AuthenticationException;
}
