package com.simple.original.server.service;


public class NotificationService {

    
    private static NotificationService instance = null;
    
    
    private NotificationService() {
        
    }
    
    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    public void processNotification(Notification notification) {
       System.out.println(" Received Notification " + notification.toString());
    }
}
