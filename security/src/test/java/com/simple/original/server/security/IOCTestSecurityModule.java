package com.simple.original.server.security;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.simple.security.IOCSecurityModule;
import com.simple.security.api.ICredentialLocator;

public class IOCTestSecurityModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new IOCSecurityModule());

		bind(SecurityManager.class).to(DefaultSecurityManager.class).in(Scopes.SINGLETON);
		bind(ICredentialLocator.class).to(TestCredentialLocator.class);
		
	}

}
