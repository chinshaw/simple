package com.simple.orchestrator.server.service;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class OperationLogHandler extends Handler {

    StringBuilder sb = new StringBuilder();
    
    
    public OperationLogHandler() {
        super();
        setFormatter(new SimpleFormatter());
        setLevel(Level.ALL);
    }
    
    @Override
    public void close() throws SecurityException {
    	clear();
        sb = null;
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        Formatter formatter = getFormatter();
        String msg = formatter.format(record);
        
        sb.append(msg);
    }
    
    public String dumpLog() {
        return sb.toString();
    }
    
    public void clear() {
    	sb.delete(0, sb.length());
    }   
}