package com.simple.original.client.service.jms.amq;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Node;

public class PlainTextMessage extends JavaScriptObject {

	protected PlainTextMessage() {
		super();
	}

	public final String getText() {
		return asNode().getNodeValue();
	}

	protected final Node asNode() {
		Node node = this.cast();
		return node;
	}
}
