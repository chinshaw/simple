package com.simple.original.server.security;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.simple.domain.IOCDomainModule;
import com.simple.domain.Person;
import com.simple.domain.dao.PersonDao;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.api.exceptions.DomainException;
import com.simple.original.security.AuthenticationException;
import com.simple.original.security.IAuthenticationProvider;
import com.simple.original.security.IOCSecurityModule;

public class AuthenticationServiceTest {

	@Inject
	private PersonDao personDao;

	@Inject
	private IAuthenticationProvider authService;

	public AuthenticationServiceTest() {
		Guice.createInjector(new IOCDomainModule(), new IOCSecurityModule())
				.injectMembers(this);

	}

	@Test(expected = AuthenticationException.class) 
	public void testDoAuthenticateFailure() throws AuthenticationException,
			DomainException {
		Person person = new Person();

		person.setEmail("chris.hinshaw@simple.com");
		person.setPassword("password");
		person.setName("Chris Hinshaw");
		person.getRoles().add(IPerson.Role.ADMIN);

		personDao.save(person);

		String username = "chris";
		String password = "password";

		authService.doAuthenticate(username, password);
	}

	@Test
	public void testDoAuthenticateSuccess() throws AuthenticationException, DomainException {
		Person person = new Person();

		person.setEmail("chris.hinshaw@simple.com");
		person.setPassword("password");
		person.setName("Chris Hinshaw");
		person.getRoles().add(IPerson.Role.ADMIN);

		personDao.save(person);

		String username = "chris.hinshaw@simple.com";
		String password = "password";

		authService.doAuthenticate(username, password);
	}
	
	@Test
	public void testAuthenticationGWT() {
		
	}
}