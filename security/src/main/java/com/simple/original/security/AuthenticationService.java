package com.simple.original.security;

import javax.inject.Singleton;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;
import com.simple.domain.Person;
import com.simple.domain.dao.PersonDao;
import com.simple.original.api.security.ISession;

@Singleton
public class AuthenticationService implements IAuthenticationProvider {

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
	@Override
	public Person doAuthenticate(String username, String password)
			throws AuthenticationException {

		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		token.setRememberMe(true);
		
		
		Subject currentUser = SecurityUtils.getSubject();

		try {
			currentUser.login(token);
			
			Session session = currentUser.getSession(true);
			
			Person person = personDao.findUser(username);
			
			System.out.println("DOING AUTHETNCATION ANS SETTING PERSON TO BE " + person.getName());
			
			logger.info("Setting current person to be " + person.getName());
			session.setAttribute(ISession.CURRENT_PERSON, person);

			return person;
		} catch (IncorrectCredentialsException ice) {
			logger.error("Incorrect credentials " + username);
			throw new AuthenticationException("invalid username or password");
		} catch (org.apache.shiro.authc.AuthenticationException ex) {
			throw new AuthenticationException("Unable to authenticate", ex);
		}
	}

	public void logout() {

	}

}
