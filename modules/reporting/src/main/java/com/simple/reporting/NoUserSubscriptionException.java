package com.simple.reporting;

public class NoUserSubscriptionException extends Exception {

	  private static final long serialVersionUID = 1L;

	  public NoUserSubscriptionException() {
	    super("No User Subscribed");
	  }

	  public NoUserSubscriptionException(String message) {
	    super(message);
	  }

}
