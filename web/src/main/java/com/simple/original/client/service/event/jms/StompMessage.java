package com.simple.original.client.service.event.jms;

import com.google.gwt.core.client.JavaScriptObject;

public final class StompMessage extends JavaScriptObject implements IJmsMessage {
	
	
	protected StompMessage(){}
	 
	 
	public final native String getBody()/*-{  
		return this.body;
	}-*/;
 
	public final native StompHeader getStompHeader()/*-{
		return this.headers;
	}-*/;
 
	public static final native StompMessage create(String json)/*-{
		return eval('(' + json + ')');
	}-*/;
	

	@Override
	public String getMessage() {
		return getBody();
	}
}
