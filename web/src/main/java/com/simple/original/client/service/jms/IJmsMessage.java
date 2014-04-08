package com.simple.original.client.service.jms;

import java.util.Map;

public interface IJmsMessage {

	
	Map<String, String> getHeaders();
	
	
	String getMessage();
}
