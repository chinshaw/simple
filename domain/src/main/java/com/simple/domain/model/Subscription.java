package com.simple.domain.model;

public class Subscription {


	private Long id;

    private String name;
 
    private String description;

    private boolean isSubscribed;

    public Subscription(Long id,String  name, String description,boolean isSubscribed) {
    	this.id = id;
    	this.name = name;
        this.description =  description;
    	this.isSubscribed=isSubscribed;
    }

    public Subscription() {

    }
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * @return the isSubscribed
	 */
	public boolean isSubscribed() {
		return isSubscribed;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


}
