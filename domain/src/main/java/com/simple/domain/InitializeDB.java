package com.simple.domain;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.simple.domain.dao.PersonDao;
import com.simple.domain.model.Person;
import com.simple.original.api.exceptions.DomainException;

public class InitializeDB {

	private static final Injector injector = Guice.createInjector(new IOCDomainModule());

	public static void main(String args[]) throws DomainException {
		Person admin = new Person("admin", "admin@simple.com");
		admin.setPassword("password");
		
		PersonDao dao = injector.getInstance(PersonDao.class);
		dao.save(admin);
	}
}
