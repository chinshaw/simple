package com.simple.original.server.servlet;

import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ShiroFilter extends AbstractShiroFilter {

	@Inject
	public ShiroFilter(WebSecurityManager webSecurityManager) {
		setSecurityManager(webSecurityManager);
	}
}
