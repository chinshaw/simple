package com.simple.api.domain;

public enum GroupMembership {

	USER("User"),
	ADMINISTRATOR("Administrator");
	
	private String name;
	
	GroupMembership(String name) {
		this.name = name;
	}

	/**
	 * Constant for admin group from ldap.
	 */
	public static final String LDAP_ADMIN = "VirtualFactoryAdmin";
	
	/**
	 * Constant name for virtual factory user.
	 */
	public static final String LDAP_USER = "VirtualFactoryUser";

	public static GroupMembership fromLdapString(String ldapGroup) {

		if (LDAP_ADMIN.equals(ldapGroup)) {
			return ADMINISTRATOR;
		}

		if (LDAP_USER.equalsIgnoreCase(ldapGroup)) {
			return USER;
		}

		return null;
	}
	
	
	public String toString() {
		return name;
	}
}
