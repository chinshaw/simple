package com.simple.original.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.simple.original.api.orchestrator.IPerson;
import com.simple.original.security.api.ISession;

public class ShiroSession implements ISession {
	
	public Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	@Override
	public IPerson getCurrentPerson() {
		return (IPerson) getSession().getAttribute(CURRENT_PERSON);
	}

	public void setCurrentPerson(IPerson currentPerson) {
		getSession().setAttribute(CURRENT_PERSON, currentPerson);
	}

	@Override
	public Long getPersonId() {
		return getCurrentPerson().getId();
	}

	@Override
	public boolean isAuthenticated() {
		return (Boolean) getSession().getAttribute(AUTHENTICATED);
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) {
		getSession().setAttribute(AUTHENTICATED, isAuthenticated);
	}

	@Override
	public void invalidate() {
		SecurityUtils.getSubject().logout();
	}

	@Override
	public void setMaxInactiveInterval(int timeout) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

}
