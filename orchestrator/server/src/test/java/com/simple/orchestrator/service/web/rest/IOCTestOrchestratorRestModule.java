package com.simple.orchestrator.service.web.rest;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.simple.api.exceptions.NoSuchUserException;
import com.simple.domain.dao.PersonDao;
import com.simple.domain.model.Person;
import com.simple.orchestrator.IOCOrchestratorModule;
import com.simple.original.domain.dao.FakeSession;
import com.simple.security.api.ISession;

public class IOCTestOrchestratorRestModule extends ServletModule {

	@Override
	public void configureServlets() {
		install(new IOCOrchestratorModule());

	}

	@Provides
	@Inject
	ISession sessionProvider(PersonDao personDao) {
		Person fakePerson = null;
		try {
			 fakePerson = personDao.findUser("fake@tester.com");
		} catch (NoSuchUserException no) {
			fakePerson = new Person("Fake User", "fake@tester.com");
			try {
				fakePerson = personDao.saveAndReturn(fakePerson);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FakeSession session = new FakeSession();
		session.setCurrentPerson(fakePerson);
		return session;
	}
}