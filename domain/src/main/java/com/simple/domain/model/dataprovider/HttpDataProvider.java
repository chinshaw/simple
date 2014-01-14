package com.simple.domain.model.dataprovider;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class HttpDataProvider extends DataProvider {

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = -5998504926291839594L;

	
	private String url;

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
}
