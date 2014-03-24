package com.simple.security;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.shiro.SecurityUtils;
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
import com.simple.api.orchestrator.IPerson;
import com.simple.security.api.ICredentialLocator;
import com.simple.security.api.IHasCredentials;

import org.apache.shiro.mgt.SecurityManager;


public class ArtisanAuthenticationRealm extends AuthorizingRealm {
	
	
	private static final Logger logger = Logger
			.getLogger(ArtisanAuthenticationRealm.class.getName());

	private ICredentialLocator credentialsLocator;
	
	@Inject
	public ArtisanAuthenticationRealm(SecurityManager securityManager, ICredentialLocator credentialsLocator) {
		this.credentialsLocator = credentialsLocator;
		SecurityUtils.setSecurityManager(securityManager);
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

		IHasCredentials person = credentialsLocator.find(username);

		// Retrieve roles and permissions from database
		Set<String> roleNames = getRolesForUser(person);

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();

		// Null username is invalid
		if (username == null) {
			throw new AccountException(
					"Null usernames are not allowed by this realm.");
		}

		IHasCredentials hasCredentials =  credentialsLocator.find(username);

		if (hasCredentials == null) {
			throw new AuthenticationException("invalid username account");
		}

		SimpleAuthenticationInfo info = null;
		String password = hasCredentials.getPassword();

		if (password == null) {
			throw new UnknownAccountException("No account found for user ["
					+ username + "]");
		}

		info = new SimpleAuthenticationInfo(username, password.toCharArray(),
				hasCredentials.getName());

		return info;
	}

	/**
	 * List of role names.
	 * 
	 * @param person
	 * @return
	 */
	protected Set<String> getRolesForUser(IHasCredentials hasCredentials) {
		Set<String> roleNames = new HashSet<String>();
		Set<IPerson.Role> roles = hasCredentials.getRoles();

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
}
