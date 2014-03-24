package com.simple.original.server;

import com.google.inject.Inject;
import com.simple.domain.dao.PersonDao;
import com.simple.security.api.ICredentialLocator;
import com.simple.security.api.IHasCredentials;

public class WebCredentialsLocator implements ICredentialLocator {

	@Inject
	PersonDao personDao;
	
	@Override
	public IHasCredentials find(String username) {
		return personDao.findUser(username);
	}

}
