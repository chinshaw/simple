package com.simple.original.client.service.jms.amq;

import com.google.gwt.core.client.JavaScriptObject;

public interface MessageHandler<MessageType extends JavaScriptObject> {
    void onMessage(MessageType message);
}
