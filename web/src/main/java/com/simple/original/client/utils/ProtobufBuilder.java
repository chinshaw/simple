package com.simple.original.client.utils;

import com.google.gwt.core.client.JavaScriptObject;

public final class ProtobufBuilder extends JavaScriptObject {

	protected ProtobufBuilder() {
	}
	
	public final native <T> T build() /*-{
		return this.build();
	}-*/;
	
	public final native <T extends ProtobufMessage> T build(String messageType) /*-{
		return this.build(messageType);
	}-*/;
}