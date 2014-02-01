package com.simple.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

import com.simple.original.api.analytics.IPerson;
import com.simple.original.api.domain.GroupMembership;
import com.simple.original.security.api.IHasCredentials;

/**
 * The person is virtual factories representation of a person / user. This
 * person object does not contain any authentication mechanism but is simply a
 * place to store preferences for a user of the system. It does contain their
 * group membership which comes from ldap. Their common name and their email as
 * a unique identifier. The database id is still a long but most methods will
 * allow you to fetch a user by their email address and email is guaranteed to
 * be unique in the database.
 * 
 * 
 * @author chinshaw
 * 
 */
@Entity
@NamedQueries(value = {
		@NamedQuery(name = "Person.findByEmail", query = "select p from Person as p where p.email = :email"),
		@NamedQuery(name = "Person.findByName", query = "select p from Person as p where p.name in (:n)") })
public class Person extends RequestFactoryEntity implements
		IPerson, IHasCredentials  {

	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = -562002510314815682L;

	@Column(name = "roles")
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private Set<IPerson.Role> roles;

	private String password;

	/**
	 * Name of User
	 */
	@Column(nullable = false)
	private String name;

	@ElementCollection
	@CollectionTable(name = "person_logins", joinColumns = @JoinColumn(name = "person_id"))
	@OrderColumn
	private List<Date> loginDates = new ArrayList<Date>();

	/**
	 * Email Id of User
	 */
	@Column(unique = true, nullable = false)
	@Index(name = "email_idx")
	@NotNull(message = "email address is a required field")
	private String email;

	/**
	 * One to one relationship to set user Preferences.
	 */
	@OneToOne(cascade = CascadeType.ALL, targetEntity = Preferences.class)
	@JoinColumn(name = "preferences_id")
	private Preferences preferences;

	public Person() {
		this(null, null);
	}

	public Person(String name, String email) {
		this(name, email, new HashSet<IPerson.Role>(), new Preferences());
	}
	
	public Person(String name, String email, Set<IPerson.Role> roles, Preferences preferences) {
		this.name = name;
		this.email = email;
		this.roles = roles;
		this.preferences = preferences;
	}

	public Set<IPerson.Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<IPerson.Role> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * This will check to see if the person has set a preferred email address in
	 * their preferences but if it is null then we will use their @hp.com email
	 * address when sending alerts.
	 * 
	 * Use {@link Preferences#setSubscriberMailId(String)} to override the
	 * default
	 * 
	 * @hp email address.
	 * @return
	 */
	public String getPreferredEmailAddress() {
		if (getPreferences().getSubscriberMailId() != null) {
			return getPreferences().getSubscriberMailId();
		}
		return email;
	}

	public Date getLastLogin() {
		if (loginDates == null) {
			return null;
		}
		return loginDates.get(loginDates.size());
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}

	public void addSuccessfulLogin() {
		loginDates.add(new Date());
	}
	
	public GroupMembership getUserGroup() {
		return null;
	}

	@Override
	public String getUserName() {
		return email;
	}
}