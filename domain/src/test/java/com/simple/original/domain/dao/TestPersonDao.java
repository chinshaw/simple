package com.simple.original.domain.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.simple.domain.IOCDomainModule;
import com.simple.domain.dao.PersonDao;
import com.simple.domain.model.Person;
import com.simple.original.api.exceptions.DomainException;
import com.simple.original.api.orchestrator.IPreferences;

public class TestPersonDao {


	private static EntityManagerFactory entityManagerFactory = null;
	
	@Inject
	PersonDao personDao;
	
	public TestPersonDao() {
		Guice.createInjector(new IOCDomainModule()).injectMembers(this);
	}

	/**
	 * Create an entity manager factory using the TestPersistenceManager
	 * factory, this database can be blown away as necessary.
	 */
	@BeforeClass
	public static void init() {
	}


	@After
	public void cleanup() {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("truncate table person cascade").executeUpdate();
		// em.createNativeQuery("truncate table preferences").executeUpdate();
		em.getTransaction().commit();
	}


	@Test
	public void testSaveWithNullEmail() throws DomainException {
		Person person = new Person();
		person.setName("testSaveWithNullEmail");

		boolean thrown = false;
		try {
			personDao.save(person);
		} catch (ConstraintViolationException violation) {
			for (ConstraintViolation<?> constraint : violation
					.getConstraintViolations()) {
				if (constraint.getMessage().contains("email address")) {
					thrown = true;
					break;
				}
			}
		}

		assertTrue(thrown);
	}

	@Test
	public void testSaveAndFindSameUser() throws DomainException {
		final String NAME = "testSaveAndFindSameUser";
		final String EMAIL = "testSaveAndFindSameUser@hp.com";

		Person person = new Person();
		person.setName(NAME);
		person.setEmail(EMAIL);
		// person.setUserGroup(GroupMembership.USER);

		// Get the user back out
		Long idOfUser = personDao.save(person);

		assertNotNull("Unable to save user", idOfUser);

		Person fromDatastore = personDao.find(idOfUser);

		assertNotNull("Unable to retrieve person from db", fromDatastore);
		assertTrue("Name is not equal", fromDatastore.getName().equals(NAME));
		assertTrue("Email is not equal", fromDatastore.getEmail().equals(EMAIL));
	}

	@Test
	public void testSaveFindUpdatePerson() throws DomainException {
		final String NAME = "testSaveFindUpdateUser";
		final String EMAIL = "testSaveFindUpdateUser@hp.com";

		Person person = new Person();
		person.setName(NAME);
		person.setEmail(EMAIL);

		// Get the user back out
		Long idOfUser = personDao.save(person);

		assertNotNull("Unable to save user", idOfUser);

		Person fromDatastore = personDao.find(idOfUser);

		assertNotNull("Unable to retrieve person from db", fromDatastore);
		assertTrue("Name is not equal", fromDatastore.getName().equals(NAME));
		assertTrue("Email is not equal", fromDatastore.getEmail().equals(EMAIL));

		fromDatastore.setName("testSaveFindUpdateUser updated");

		Long updatedId = personDao.save(fromDatastore);

		assertNotNull("Unable to save person again", updatedId);
		assertSame(updatedId, idOfUser);

		// Go get it again
		fromDatastore = personDao.find(updatedId);

		assertTrue("Name change was not successful", fromDatastore.getName()
				.equals("testSaveFindUpdateUser updated"));
	}

	@Test
	public void testDeleteUser() throws DomainException {
		final String NAME = "testDeleteUser";
		final String EMAIL = "testDeleteUser@hp.com";

		Person person = new Person();
		person.setName(NAME);
		person.setEmail(EMAIL);

		// Get the user back out
		Long idOfUser = personDao.save(person);

		assertNotNull("Unable to save user", idOfUser);

		Person fromDatastore = personDao.find(idOfUser);

		assertNotNull("Unable to retrieve person from db", fromDatastore);
		assertTrue("Name is not equal", fromDatastore.getName().equals(NAME));
		assertTrue("Email is not equal", fromDatastore.getEmail().equals(EMAIL));

		personDao.delete(idOfUser);

		Person shoudlBeNull = personDao.find(idOfUser);

		assertTrue("User was not deleted", (shoudlBeNull == null));
	}

	/**
	 * Test method FindPerson: to retrieve all the persons.
	 * 
	 * @Test public void testSearchPersonByName() { final String NAME =
	 *       "testSearchPersonByName"; final String EMAIL =
	 *       "testSearchPersonByName@hp.com";
	 * 
	 *       Person person = new Person(EMAIL); person.setName(NAME); Long
	 *       idOfPerson = personDao.save(person);
	 * 
	 *       assertNotNull("Unable to save the initial person in database",
	 *       idOfPerson);
	 * 
	 * 
	 *       String searchText = NAME; String sortColumn = "name"; String
	 *       searchColumn = "name";
	 * 
	 *       System.out.println("DOing query"); List<Person> personList =
	 *       personDao.search(start, max, null, searchText, searchColumn,
	 *       sortColumn, SortOrder.ASCENDING);
	 * 
	 *       assertNotNull("search returned 0 results", personList);
	 *       assertFalse("search returned 0 results", personList.isEmpty());
	 * 
	 *       personDao.delete(idOfPerson); }
	 */

	/**
	 * Create a person store them in the db and search for that user using the
	 * search function.
	 * 
	 * @Test public void testSearchPersonByEmail() { final String NAME =
	 *       "testSearchPersonByEmail"; final String EMAIL =
	 *       "testSearchPersonByEmail@hp.com";
	 * 
	 *       Person person = new Person(EMAIL); person.setName(NAME); Long
	 *       idOfPerson = personDao.save(person);
	 * 
	 *       assertNotNull("Unable to save the initial person in database",
	 *       idOfPerson);
	 * 
	 *       String searchText = NAME; String sortColumn = "email"; String
	 *       searchColumn = "email";
	 * 
	 *       List<Person> personList = personDao.search(start, max, null,
	 *       searchText, searchColumn, sortColumn, SortOrder.ASCENDING);
	 * 
	 *       assertNotNull("search returned 0 results", personList);
	 *       assertFalse("search returned 0 results", personList.isEmpty()); }
	 */

	/**
	 * This will create a new person store them in the database with a specific
	 * preference email address and then pull that user back out of the database
	 * re assign a new subscriber email address and then persist that user back
	 * into the database, them pull it out again and make sure that the email
	 * address was updated correctly.
	 * @throws DomainException 
	 */
	@Test
	public void testSavePreferences() throws DomainException {
		final String NAME = "testSavePreferences";
		final String EMAIL = "testSavePreferences@hp.com";
		final String PREF_EMAIL = "testSavePreferences2@hp.com";

		Person person = new Person(NAME, EMAIL);
		person.setName(NAME);

		person.getPreferences().setSubscriberMailId(PREF_EMAIL);

		Long idOfPerson = personDao.save(person);

		Person fromDb = personDao.find(idOfPerson);

		assertTrue("subscriber id did not match", fromDb.getPreferences()
				.getSubscriberMailId().equals(PREF_EMAIL));

		IPreferences prefs = person.getPreferences();
		prefs.setSubscriberMailId("testSavePreferences3@hp.com");

		Long idNow = personDao.save(fromDb);
		assertTrue("Ids don't match there is probably another person now",
				idOfPerson.equals(idNow));

		// Go find it again
		fromDb = personDao.find(idNow);

		assertTrue("Preference email was wrong", fromDb.getPreferences()
				.getSubscriberMailId().equals("testSavePreferences3@hp.com"));
	}
}