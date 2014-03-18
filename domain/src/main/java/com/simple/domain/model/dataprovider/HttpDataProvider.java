package com.simple.domain.model.dataprovider;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the http data provider definition, it allows you
 * to load data into a job from the web.
 * @author chris
 *
 */
@Entity
@XmlRootElement(name ="httpdataprovider")
public class HttpDataProvider extends DataProvider {

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = -5998504926291839594L;

	private String url;
	
	private String username;
	
	private String password;
	
	public HttpDataProvider() {
	}

	public HttpDataProvider(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
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
}