package com.simple.original.client.utils;

import com.google.gwt.core.client.JavaScriptObject;

public class ProtobufMessage extends JavaScriptObject {

	protected ProtobufMessage() {
	}
	
	public final native <T extends ProtobufMessage> T decode(byte[] bytes) /*-{
		return this.decode(bytes);
	}-*/;
	
	public final native <T extends ProtobufMessage> T decode64(byte[] bytes) /*-{
		return this.decode64(bytes);
	}-*/;
}