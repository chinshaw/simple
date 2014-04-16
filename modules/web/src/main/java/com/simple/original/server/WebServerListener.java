package com.simple.original.server;

import java.io.IOException;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;

public class WebServerListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent context) {
        
    }

    @Override
    public void contextInitialized(ServletContextEvent context) {
        LogManager logManager = LogManager.getLogManager();
        try {
            logManager.readConfiguration(Thread.currentThread().getClass().getResourceAsStream("/META-INF/logging.properties"));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}