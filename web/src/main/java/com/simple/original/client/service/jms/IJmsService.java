package com.simple.original.client.service.jms;

public interface IJmsService {

	interface MessageHandler {
		void onMessage(IJmsMessage messge);
	}

	public void addMessageHandler(MessageHandler messageHandler);

}
