package com.simple.domain.model.dataprovider;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import com.simple.domain.model.DatastoreObject;

@Entity
@XmlRootElement
public class DBConnection extends DatastoreObject {
	
	/**
	 * Serialization Id
	 */
	private static final long serialVersionUID = 6580039851295600380L;

	private String username;
	
	private String password;
	
	private String url;
	
	public DBConnection() {
		
	}
	
	public DBConnection(String username, String password, String url) {
		this.username = username;
		this.password = password;
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
