package com.simple.original.client.service.event.jms;

import java.net.MalformedURLException;

public interface IJmsService {

	interface MessageHandler {
		void onMessage(IJmsMessage message);
	}

	public void addMessageHandler(MessageHandler messageHandler);

	public void setUrl(String string) throws MalformedURLException;

	public String getUrl();
	
	public void setConnectionCallback(ConnectionCallback callback);


}
