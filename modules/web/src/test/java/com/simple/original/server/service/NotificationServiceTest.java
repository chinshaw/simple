package com.simple.original.server.service;

import java.util.HashMap;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.simple.original.server.service.Notification;
import com.simple.original.server.service.NotificationService;
import com.simple.original.server.service.Notification.ParameterMap;

public class NotificationServiceTest {

    
    @Test
    public void createNotification() {
        final Map<String, String> parameters = new HashMap<String,String>();
        
        parameters.put("NotificationType", "Alert");
        parameters.put("AlertName", "The name of the alert");
        parameters.put("AlertText", "THis is the alert detail");
        
        Notification notification = Notification.fromParameterMap(new ParameterMap() {

            @Override
            public String get(String param) {
                return parameters.get(param);
            }
        });
        
        NotificationService.getInstance().processNotification(notification);
        
        assertNotNull("notification is null", notification);        
    }
}