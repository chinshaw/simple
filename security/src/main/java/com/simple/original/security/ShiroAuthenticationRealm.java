package com.simple.original.security;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.google.inject.Inject;
import com.simple.domain.Person;
import com.simple.domain.dao.PersonDao;
import com.simple.original.api.analytics.IPerson;


public class ShiroAuthenticationRealm extends AuthorizingRealm {
	
	
	private static final Logger logger = Logger
			.getLogger(ShiroAuthenticationRealm.class.getName());

	private PersonDao personDao;
	
	public ShiroAuthenticationRealm() {
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {

		logger.info("Doing auth");
		// null usernames are invalid
		if (principals == null) {
			throw new AuthorizationException(
					"PrincipalCollection method argument cannot be null.");
		}

		String username = (String) getAvailablePrincipal(principals);

		Set<String> permissions = null;

		Person person = (Person) personDao.findUser(username);

		// Retrieve roles and permissions from database
		Set<String> roleNames = getRolesForUser(person);

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;
	}

	
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();

		// Null username is invalid
		if (username == null) {
			throw new AccountException(
					"Null usernames are not allowed by this realm.");
		}

		Person person = (Person) personDao.findUser(username);

		if (person == null) {
			throw new AuthenticationException("invalid username account");
		}

		SimpleAuthenticationInfo info = null;
		String password = person.getPassword();

		if (password == null) {
			throw new UnknownAccountException("No account found for user ["
					+ username + "]");
		}

		info = new SimpleAuthenticationInfo(username, password.toCharArray(),
				person.getName());

		return info;
	}

	/**
	 * List of role names.
	 * 
	 * @param person
	 * @return
	 */
	protected Set<String> getRolesForUser(Person person) {
		Set<String> roleNames = new HashSet<String>();
		Set<IPerson.Role> roles = person.getRoles();

		if (roles != null && roles.size() > 0) {

			roleNames = new HashSet<String>();
			for (IPerson.Role role : roles) {
				roleNames.add(role.name());
			}
		}
		return roleNames;
	}

	protected String getSaltForUser(String username) {
		return username;
	}
	
	@Inject
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}
	
}
