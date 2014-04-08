package com.simple.original.client.service.jms.amq;

import com.google.gwt.core.client.JavaScriptObject;
/**
 * This is deprecated and is here only temporarily.
 * It has a problemw ith taking a really long time to respond when a message is sent
 * 
 * 
 * @deprecated
 * @author chris
 *
 */
public final class ActiveMQ extends JavaScriptObject {

	protected ActiveMQ() {
	}

	public static native ActiveMQ get() /*-{
		return $wnd.org.activemq.Amq
	}-*/;

	public native void initialize(String uri) /*-{
		this.init({
			uri: uri,
			logging: true,
			timeout: 45,
			clientId:(new Date()).getTime().toString()
		});
	}-*/;

	public MessageHandlerRegistration subscribe(String destination, MessageHandler<?> handler) {
		final String id = nextId();
		doSubscribe(id, destination, handler);
		return new MessageHandlerRegistration() {
			public void removeHandler() {
				doUnsubscribe(id);
			}
		};
	}

	private native String nextId() /*-{
		if (!this.jcfNextId) {
			this.jcfNextId = 0
		}
		this.jcfNextId++
		return '' + this.jcfNextId
	}-*/;

	public native void send(String destination, String message) /*-{
		this.sendMessage(destination, message)
	}-*/;

	private native void doSubscribe(String id, String destination, MessageHandler<?> handler) /*-{
		var callback = {
			receive : function(message) {
				handler.@com.simple.original.client.service.jms.amq.MessageHandler::onMessage(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
			}
		};
		this.addListener(id, destination, callback.receive);
	}-*/;

	private native void doUnsubscribe(String id) /*-{
		this.removeHandler(id);
	}-*/;

}
