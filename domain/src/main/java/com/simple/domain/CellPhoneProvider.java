package com.simple.domain;


import javax.xml.bind.annotation.XmlRootElement;


/**
 * Cell phone provider properties
 */
@XmlRootElement
public class CellPhoneProvider  {
	

	/**
	 * Unique Id for each cellphone provider
	 */
	private Long id;

	/**
	 * CellPhone Provider Name for CellPhoneProvider
	 */
	private String name;
	
	/**
	 * Postfix of email for each CellPhoneProvider
	 */
	
	private String emailPostfix;
	
	public String getEmailPostfix() {
		return emailPostfix;
	}

	public void setEmailPostfix(String emailPostfix) {
		this.emailPostfix = emailPostfix;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}