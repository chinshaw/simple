package com.simple.original.domain.dao;

import javax.persistence.EntityManager;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.simple.domain.IOCDomainModule;
import com.simple.domain.dao.PersonDao;
import com.simple.domain.model.Person;
import com.simple.original.api.exceptions.DomainException;
import com.simple.original.api.exceptions.NoSuchUserException;
import com.simple.original.domain.IOCDomainTestModule;

public class DaoTest {

	//private static final String TEST_USER_EMAIL = "testuser@testdomain.com";

	private static FakeSession sessionTestImpl = new FakeSession();

	static Person testPerson;

	@Inject
	PersonDao personDao;

	public DaoTest() throws DomainException {
		Module modules = Modules.override(new IOCDomainModule()).with(
				new IOCDomainTestModule());

		Injector injector = Guice.createInjector(modules);
		injector.injectMembers(this);

		try {
			testPerson = personDao.findUser("testuser@testdomain.com");
		} catch (NoSuchUserException ne) {
			testPerson = new Person("Test User", "testuser@testdomain.com");
			testPerson = personDao.saveAndReturn(testPerson);
		}
		sessionTestImpl.setCurrentPerson(testPerson);
	}

	public static void truncateTable(EntityManager em, String tableName) {
		em.getTransaction().begin();
		em.createNativeQuery("truncate table " + tableName + " cascade")
				.executeUpdate();
		em.getTransaction().commit();
	}

	public static void truncateAllTables(EntityManager em) {
		truncateTable(em, "person");
		truncateTable(em, "analyticstask");
		// truncateTable(em, "analyticsoperation_inputs");
	}

}
