package com.simple.original.server.service;

import com.simple.original.server.service.Notification.AlertNotification;
import com.simple.original.server.service.Notification.NotificationType;

public class NotificationFactory {

    public static Notification createNotification(NotificationType type) {
        switch (type) {
        case Alert:
            return new AlertNotification();
        }
        return null;
    }
}
