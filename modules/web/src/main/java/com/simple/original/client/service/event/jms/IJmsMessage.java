package com.simple.original.client.service.event.jms;


public interface IJmsMessage {

	StompHeader getStompHeader();

	String getMessage();
}
