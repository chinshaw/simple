package com.simple.original.server.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.simple.original.api.exceptions.DomainException;
import com.simple.original.security.ArtisanAuthenticationRealm;

public class AuthenticationServiceTest {

	@Inject
	ArtisanAuthenticationRealm realm;

	public AuthenticationServiceTest() {
		Guice.createInjector(new IOCTestSecurityModule()).injectMembers(this);
		SecurityUtils.setSecurityManager(new DefaultSecurityManager(realm));
	}

	@Test(expected = AuthenticationException.class)
	public void testDoAuthenticateFailure() throws AuthenticationException,
			DomainException {

		UsernamePasswordToken token = new UsernamePasswordToken("adf",
				"password");

		Subject currentUser = SecurityUtils.getSubject();

		currentUser.login(token);
	}

	@Test
	public void testDoAuthenticateSuccess() throws AuthenticationException,
			DomainException {

		String username = "testuser@test.com";
		String password = "password";

		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password);

		Subject currentUser = SecurityUtils.getSubject();
		currentUser.login(token);
	}
}