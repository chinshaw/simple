package com.simple.original.server.service;

import java.util.logging.Logger;

import javax.inject.Singleton;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;
import com.simple.domain.dao.PersonDao;
import com.simple.domain.model.Person;
import com.simple.original.api.analytics.IPerson;
import com.simple.original.security.AuthenticationException;
import com.simple.original.security.api.ISession;

@Singleton
public class AuthenticationService {

	private static final Logger logger = Logger
			.getLogger(AuthenticationService.class.getName());

	private final PersonDao personDao;

	@Inject
	public AuthenticationService(PersonDao personDao) {
		this.personDao = personDao;
	}

	/**
	 * Authenticate a user and return the datastore person from the database,
	 * this method uses the ISecurity provider to accomplish authentication.
	 * 
	 * @param email
	 *            email address of the user.
	 * @param password
	 *            password of the user
	 * @return If user is found the authenticated user.
	 * @throws PasswordAuthenticationFailure
	 *             if there is a problem authenticating.
	 * @throws AuthenticationException
	 */
	public Person doAuthenticate(String username, String password)
			throws AuthenticationException {

		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		token.setRememberMe(true);
		
		Subject currentUser = SecurityUtils.getSubject();

		try {
			currentUser.login(token);
			
			Session session = currentUser.getSession(true);
			
			Person person = personDao.findUser(username);
			logger.info("Setting current person to be " + person.getName());
			session.setAttribute(ISession.CURRENT_PERSON, person);

			return person;
		} catch (IncorrectCredentialsException ice) {
			logger.info("Incorrect credentials " + username);
			throw new AuthenticationException("invalid username or password");
		} catch (org.apache.shiro.authc.AuthenticationException ex) {
			throw new AuthenticationException("Unable to authenticate", ex);
		}
	}

	public void logout() {

	}
	
	public IPerson getCurrentPerson() throws AuthenticationException {
		Subject currentUser = SecurityUtils.getSubject();
		if (! currentUser.isAuthenticated()) {
			throw new AuthenticationException();
		}
		
		Session session = currentUser.getSession();
		IPerson person = (IPerson) session.getAttribute(ISession.CURRENT_PERSON);
		
		logger.info("The person is " + person);
		return person;
	}
}
