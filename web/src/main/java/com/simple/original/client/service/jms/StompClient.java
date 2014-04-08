package com.simple.original.client.service.jms;

import com.google.gwt.user.client.Window;

public class StompClient implements IJmsService {
	private String url;

	private ConnectionCallback callback;

	public StompClient(String url) {
		this(url, null);
	}

	public StompClient(String url, ConnectionCallback callback) {
		this.url = url;
		this.callback = callback;
		init();
	}

	private native final void init()/*-{
		$wnd.subscriptions = new Array();
		$wnd.stompClient = $wnd.Stomp
				.client(this.@com.simple.original.client.service.jms.StompClient::url);
	}-*/;

	/**
	 * Connects to the JMS broker and invokes the callback interface if one was
	 * provided
	 */
	public native final void connect() /*-{
		var self = this;
		var onsuccess = function(frame) {
			try {
				alert(frame);
				self.@com.simple.original.client.service.jms.StompClient::onConnect()();
				alert(self.@com.simple.original.client.service.jms.StompClient::onConnect()());
			} catch (err) {
				alert("SHITS BROKEN " + err);
			}
		}
		var onfail = function(cause) {
			alert("Calling fail");
			self.@com.simple.original.client.service.jms.StompClient::onError(Lcom/simple/original/client/service/jms/StompMessage;)(cause);
		}
		$wnd.stompClient.connect('guest', 'guest', onsuccess, onfail);
	}-*/;

	/**
	 * Disconnects from the server and removes any subscriptions that are still
	 * active
	 */
	public native final void disconnect() /*-{
		var self = this;
		if ($wnd.subscriptions.length > 0) {
			for (var i = 0; i < $wnd.subscriptions.length; i++) {
				$wnd.stompClient.unsubscribe($wnd.subscriptions[i]);
			}
		}
		var ondisconnect = function() {
			self.@com.simple.original.client.service.jms.StompClient::onDisconnect()();
		}
		$wnd.stompClient.disconnect(ondisconnect);
	}-*/;

	/**
	 * Subscribes the given listener to a certain destination. An identifier
	 * from the subscription is returned that id should be used to unsubscribe
	 * from the channel
	 * 
	 * @param channel
	 *            - The name of the Queue/Topic
	 * @param listener
	 *            - Implementation of your message Listener
	 * @return Subscription Identifier
	 */
	public native final String subscribe(String channel, MessageHandler handler)/*-{
		var onmessage = function(message) {
			//listener.@com.simple.original.client.service.jms.MessageListener::onMessage(Ljava/lang/String;)(message);
			listener.@com.simple.original.client.service.jms.IJmsService.MessageHandler::onMessage(Lcom/simple/original/client/service/jms/IJmsMessage;)(message);
		}
		
		var id = null;
		try {
			alert("doing subscribe");
			id = $wnd.stompClient.subscribe(channel, onmessage);
			$wnd.subscriptions.push(id);
			alert("Done with subscribe");
		} catch (err) {
			alert("unable to connect");
		}
		return id;
	}-*/;

	/**
	 * Unsubscribe from the channel.
	 * 
	 * @param subscriptionId
	 *            The id of the subscription to be unsubscribed
	 */
	public native final void unsubscribe(String subscriptionId)/*-{
		var idx = $wnd.subscriptions.indexOf(subscriptionId);
		$wnd.subscriptions.splice(idx, 1);
		$wnd.stompClient.unsubscribe(subscriptionId);
	}-*/;

	/**
	 * Sends a message with the headers to a destination
	 * 
	 * @param destination
	 *            - The id of the Channel
	 * @param message
	 *            - The Message with the headers
	 */
	public native final void send(String destination, StompMessage message)/*-{
		$wnd.stompClient.send(destination, message.headers, message.body);
	}-*/;

	/**
	 * Sends a text to a destination.
	 * 
	 * @param destination
	 *            - The id of the Channel
	 * @param body
	 *            - The message contents as a String
	 */
	public native final void send(String destination, String body) /*-{
		$wnd.stompClient.send(destination, {}, body);
	}-*/;

	void onConnect() {
		Window.alert("Calling callback");
		if (callback != null) {
			callback.onConnect();
		}
	}

	void onError(StompMessage cause) {
		if (callback != null) {
			callback.onError(cause);
		}
	}

	void onDisconnect() {
		if (callback != null) {
			callback.onDisconnect();
		}
	}

	@Override
	public void addMessageHandler(MessageHandler messageHandler) {
		// TODO Auto-generated method stub
		
	}
}
