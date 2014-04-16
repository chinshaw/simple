package com.simple.original.client.service.event.jms;

import com.google.gwt.core.client.JavaScriptObject;

public class StompHeader extends JavaScriptObject {
	
	protected StompHeader() {
	}

	public final String getDestination() {
		return get("destination");
	}
	
	public final String getExpires() {
		return get("expires");
	}

	public final String getSubscription() {
		return get("subscription");
	}

	public final String getId() {
		return get("message-id");
	}
	
	public final native String getPriority()/*-{
		return this.priority;
	}-*/;

	public final String getTimestamp() {
		return get("timestamp");
	}
	
	public final native String get(String header) /*-{
		return this[header];
	}-*/;
}
