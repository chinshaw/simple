package com.simple.security.api;

import com.simple.api.orchestrator.IPerson;

public interface ISession {

	public static final String USER_ID = "uid";

	public static final String CURRENT_PERSON = "currentperson";

	public static final String AUTHENTICATED = "authenticated";

	public static final String AUTH_TOKEN = "authtoken";

	public static final String USER_ROLE = "userrole";

	public static final String USER_GROUP = "usergroup";

	public static final String COMET_SESSION_UUID = "cometuuid";

	public IPerson getCurrentPerson();

	public Long getPersonId();

	public boolean isAuthenticated();

	public void setAuthenticated(boolean isAuthenticated);

	public void invalidate();

	public void setMaxInactiveInterval(int timeout);

	public String getSessionId();
}
