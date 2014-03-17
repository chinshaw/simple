package com.simple.original.domain.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.simple.original.api.orchestrator.IPerson;
import com.simple.original.security.api.ISession;

public class FakeSession implements ISession {

	private static Map<String, Object> sessionMap = new ConcurrentHashMap<String, Object>();

	private static final String SESSION_ID = "testSession";

	@Override
	public IPerson getCurrentPerson() {
		return (IPerson) sessionMap.get("PERSON");
	}

	public void setCurrentPerson(IPerson currentPerson) {
		sessionMap.put("PERSON", currentPerson);
	}

	@Override
	public Long getPersonId() {
		return getCurrentPerson().getId();
	}

	@Override
	public boolean isAuthenticated() {
		return (Boolean) sessionMap.get("ISAUTHENTICATED");
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) {
		sessionMap.put("ISAUTHENTICATED", isAuthenticated);
	}

	@Override
	public void invalidate() {
		sessionMap.clear();
	}
	
	@Override
	public void setMaxInactiveInterval(int timeout) {
		sessionMap.put("TIMEOUT", timeout);

	}

	@Override
	public String getSessionId() {
		return SESSION_ID;
	}

}
