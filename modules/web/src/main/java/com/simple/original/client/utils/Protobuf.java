package com.simple.original.client.utils;

import com.google.gwt.core.client.JavaScriptObject;

public final class Protobuf extends JavaScriptObject {

	
	public static final native ProtobufBuilder loadFile(String file) /*-{
		return Protobuf.loadProtoFile(file);
	}-*/;
	
	public static final native Protobuf create() /*-{
		return $wnd.decodeIO.ProtoBuf;
	}-*/;
}
