package com.simple.original.shared;

import java.io.Serializable;

public enum NotificationCriticality implements Serializable {

	INFO,
	WARN,
	CRITICAL;

	
	NotificationCriticality() {
	}
}
