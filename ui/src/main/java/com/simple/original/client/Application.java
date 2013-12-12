package com.simple.original.client;

import com.simple.original.client.proxy.PersonProxy;

public class Application {

	private PersonProxy person;
	
	public Application() {
		
	}
	
	public PersonProxy getCurrentPerson() {
		return person;
	}
	
	public void setCurrentPerson(PersonProxy person) {
		this.person = person;
	}

}
