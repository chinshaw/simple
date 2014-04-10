package com.simple.original.client.service.event.jms;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;

public final class StompMessage extends JavaScriptObject implements IJmsMessage {
	
	
	protected StompMessage(){}
	 
	 
	public final native String getBody()/*-{  
		return this.body;
	}-*/;
 
	public final native StompHeader getStompHeaders()/*-{
		return this.headers;
	}-*/;
 
	public static final native StompMessage create(String json)/*-{
		return eval('(' + json + ')');
	}-*/;
	
	
	public final Map<String, String> getHeaders() {
		StompHeader header = getStompHeaders();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("destination", header.getDestination());
		headers.put("expires", header.getExpires().toString());
		headers.put("subscription", header.getSubscription());
		headers.put("id", header.getId());
		headers.put("priority", header.getPriority().toString());
		headers.put("timestamp", header.getTimestamp().toString());
		
		return headers;
	}

	@Override
	public String getMessage() {
		return getBody();
	}
}
