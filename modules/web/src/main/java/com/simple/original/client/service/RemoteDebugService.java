package com.simple.original.client.service;

import java.util.logging.Logger;

import com.google.gwt.logging.client.SimpleRemoteLogHandler;

public class RemoteDebugService {

	private static SimpleRemoteLogHandler remoteLogHandler = null;
	
    public static boolean enable() {
        Logger rootLogger = Logger.getLogger("");
        boolean enabled = false;
        if (remoteLogHandler == null) {
            remoteLogHandler = new SimpleRemoteLogHandler();
            rootLogger.addHandler(remoteLogHandler);
            rootLogger.info("Remote logging handler enabled");
            enabled = true;
        } else {
            rootLogger.removeHandler(remoteLogHandler);
            remoteLogHandler = null;
            rootLogger.info("Remote logging handler disabled");
        }
        return enabled;
    }
    
    public static boolean disable() {
        Logger rootLogger = Logger.getLogger("");
        boolean disabled = false;
        if (remoteLogHandler != null) {
            rootLogger.removeHandler(remoteLogHandler);
            disabled = true;
        } 
        return disabled;
    }
}
