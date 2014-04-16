package com.simple.security.api;

import com.simple.api.domain.GroupMembership;
import com.simple.api.orchestrator.IPerson;
import com.simple.security.AuthenticationException;

public interface ISecurity {

    public abstract IPerson createNewPerson(String email) throws Exception;
    
    public abstract boolean authenticate(String user, String password) throws AuthenticationException;
    
    public abstract ISession getSession();

    /**
     * This is an administrative function that will log out a user, this
     * call will be restricted to administrative users.
     * 
     * @param user
     */
    public abstract void logout(IPerson user);

    /**
     * Clear the session an log out the user.
     */
    public abstract void logoutCurrentUser();

    /**
     * Retrieves the current logged in person, this should be trustable
     * since it is based on the authenticated session. We store the person in
     * an environment variable when logged in, this is the current user.
     * @return Current user logged in.
     */
    public abstract IPerson getCurrentPerson();

    public abstract boolean isUserAuthenticated();

    public abstract GroupMembership getSessionUserGroup();
}
