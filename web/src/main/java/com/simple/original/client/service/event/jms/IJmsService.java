package com.simple.original.client.service.event.jms;


public interface IJmsService {

	interface MessageHandler {
		void onMessage(IJmsMessage message);
	}

	public void addMessageHandler(String queue, MessageHandler messageHandler);

	public void setUrl(String string);

	public String getUrl();
	
	public void setConnectionCallback(ConnectionCallback callback);
	
	public void start();

	public void disconnect();


}
