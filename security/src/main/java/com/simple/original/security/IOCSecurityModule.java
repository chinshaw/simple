package com.simple.original.security;

import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.simple.original.security.api.ISession;

public class IOCSecurityModule extends AbstractModule {


	@Override
	protected void configure() {
		
		bind(Realm.class).to(ArtisanAuthenticationRealm.class).in(Scopes.SINGLETON);
		// bindRealm().toConstructor(IniRealm.class.getConstructor(Ini.class));
		bind(ISession.class).to(ShiroSession.class);
		// Have to expose the ISession for the dao classes.
	}
	
	
    @Provides
    @Singleton
    WebSecurityManager provideSecurityManager(Realm realm, SessionManager sessionManager, RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager result = new DefaultWebSecurityManager(realm);
        result.setSessionManager(sessionManager);
        result.setRememberMeManager(rememberMeManager);
        return result;
    }
 
    @Provides
    @Singleton
    SessionManager provideSessionManager() {
        return new ServletContainerSessionManager();
    }
 
    @Provides
    @Singleton
    RememberMeManager provideRememberMeManager() {
        return new CookieRememberMeManager();
    }
}
