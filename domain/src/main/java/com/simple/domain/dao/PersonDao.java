package com.simple.domain.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.simple.domain.model.ApplicationBookmark;
import com.simple.domain.model.Person;
import com.simple.domain.model.Preferences;
import com.simple.original.api.exceptions.NoSuchUserException;
import com.simple.original.api.exceptions.NotAuthenticatedException;
import com.simple.original.api.orchestrator.IPerson;
import com.simple.original.security.api.IHasCredentials;

/**
 * PersonDao is the means of performing crud updates on person objects in our
 * data store. This has a few other methods not in the DaoBase class to find
 * current user, update preferences and such.
 * 
 * @author chinshaw
 * 
 */
public class PersonDao extends DaoBase<Person> {

	public PersonDao() {
		super(Person.class);
	}

	/**
	 * Find a user by email address.
	 * 
	 * @param email
	 *            email address to find.
	 * @return the user that is found.
	 * @throws NotAuthenticatedException
	 */
	public Person findUser(String email) throws NoSuchUserException {
		if (email == null) {
			throw new IllegalArgumentException(
					"A valid email address is required");
		}

		TypedQuery<IPerson> query = getEntityManager().createNamedQuery(
				"Person.findByEmail", IPerson.class);
		query.setParameter("email", email);

		Person user = null;
		try {
			user = (Person) query.getSingleResult();
		} catch (NoResultException e) {
			throw new NoSuchUserException("User with email " + email
					+ " does not exist");
		}
		// uncomment below code when you need group value for user
		// user.setUserGroup(getUserGroup(user.getEmail()));
		return user;
	}

	/**
	 * Get the preferences for the current user.
	 * 
	 * @return
	 */
	public Preferences getPreferencesForCurrentUser() {
		return getCurrentPerson().getPreferences();
	}

	/**
	 * Save the preferences object for the current user.
	 * 
	 * @param preferences
	 */
	public void savePreferences(Preferences preferences) {
		Person person = (Person) getCurrentPerson();
		person.setPreferences(preferences);
		person = super.saveOrUpdate(person);
	}

	/**
	 * Add a bookmark to the current person
	 * 
	 * @param bookmark
	 */
	public boolean addBookmark(ApplicationBookmark bookmark) {
		EntityManager em = getEntityManager();
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}

		Person person = em.merge(getCurrentPerson());
		if (person == null) {
			throw new RuntimeException(
					"Unable to add bookmark because current user is null");
		}

		Preferences prefs = person.getPreferences();

		if (prefs == null) {
			throw new RuntimeException(
					"Unable to add bookmark because current user's preferences are null");
		}

		boolean added = prefs.addBookmark(bookmark);

		em.getTransaction().commit();

		return added;
	}

	public boolean removeBookmark(ApplicationBookmark bookmark) {
		EntityManager em = getEntityManager();

		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}

		Person person = em.merge(getCurrentPerson());
		bookmark = em.find(ApplicationBookmark.class, bookmark.getId());

		Preferences prefs = person.getPreferences();
		boolean removed = prefs.removeBookmark(bookmark);

		em.getTransaction().commit();
		return removed;
	}
}
