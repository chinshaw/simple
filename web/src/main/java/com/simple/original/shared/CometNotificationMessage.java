package com.simple.original.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CometNotificationMessage implements Serializable, IsSerializable {
	
	
	
	
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 3562726113849706028L;
	
	private NotificationCriticality criticality;
	
	private String message;
	
	private CometNotificationMessage() {}
	
	public CometNotificationMessage(NotificationCriticality criticality, String message) {
		this();
		this.setCriticality(criticality);
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public NotificationCriticality getCriticality() {
		return criticality;
	}

	private void setCriticality(NotificationCriticality criticality) {
		this.criticality = criticality;
	}
}
