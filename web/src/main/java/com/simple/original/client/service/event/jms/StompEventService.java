package com.simple.original.client.service.event.jms;

import java.net.MalformedURLException;

import com.google.gwt.user.client.Window;

public class StompEventService implements IJmsService {

	private String url;

	private ConnectionCallback callback;

	public StompEventService() {

	}

	public StompEventService(String url, ConnectionCallback callback) {
		this.url = url;
		this.callback = callback;
	}

	public native final void init()/*-{
		$wnd.subscriptions = new Array();
		$wnd.stompClient = $wnd.Stomp
				.client(this.@com.simple.original.client.service.event.jms.StompEventService::url);
	}-*/;

	public void start(String url, ConnectionCallback callback) {
		this.url = url;
		this.callback = callback;
		init();
	}

	/**
	 * Connects to the JMS broker and invokes the callback interface if one was
	 * provided
	 */
	private native final void connect() /*-{
		var self = this;
		var onsuccess = function(frame) {
			try {
				alert(frame);
				self.@com.simple.original.client.service.event.jms.StompEventService::onConnect()();
				alert(self.@com.simple.original.client.service.event.jms.StompEventService::onConnect()());
			} catch (err) {
				alert("SHITS BROKEN " + err);
			}
		}
		var onfail = function(cause) {
			alert("Calling fail");
			self.@com.simple.original.client.service.event.jms.StompEventService::onError(Lcom/simple/original/client/service/event/jms/StompMessage;)(cause);
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
			self.@com.simple.original.client.service.event.jms.StompEventService::onDisconnect()();
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
			listener.@com.simple.original.client.service.event.jms.IJmsService.MessageHandler::onMessage(Lcom/simple/original/client/service/event/jms/IJmsMessage;)(message);
		}

		var id = null;
		try {
			id = $wnd.stompClient.subscribe(channel, onmessage);
			$wnd.subscriptions.push(id);
		} catch (err) {
			alert("unable to subscribe to url " + this.url + " with channel "
					+ channel + " Error " + err);
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
		this.subscribe("com.artisan.web.events", messageHandler);
	}

	/**
	 * Set the base ws url for the stomp service. Note this must be a ws:// url
	 * and not an http url to a public accessible jms queue that supports stomp.
	 * 
	 * @throws MalformedURLException
	 */
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @see StompEventService#setUrl(String)
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * The connection will respond with on connect, disconnect and error. This
	 * will be useful for debugging connection problems and for validating that
	 * the connection is connected before trying to subscribe.
	 */
	@Override
	public void setConnectionCallback(ConnectionCallback callback) {
		this.callback = callback;
	}

	/**
	 * @see #setConnectionCallback(ConnectionCallback)
	 * @return
	 */
	public ConnectionCallback getConnectionCallback() {
		return this.callback;
	}

	@Override
	public void start() {
		if (this.url == null) {
			throw new IllegalStateException("Url must be provided");
		}

		init();
	}
}
