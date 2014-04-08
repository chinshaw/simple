package com.simple.original.server.service;

public abstract class Notification {

	public interface ParameterMap {
		String get(String param);
	}

	public enum NotificationType {
		Alert
	}

	public static class AlertNotification extends Notification {

		private static final String INIT_ALERT_NAME = "AlertName";

		private static final String INIT_ALERT_DETAIL = "AlertText";

		private String alertName;

		private String alertDetail;

		public String getAlertName() {
			return alertName;
		}

		public void setAlertName(String alertName) {
			this.alertName = alertName;
		}

		public String getAlertDetail() {
			return alertDetail;
		}

		public void setAlertDetail(String alertDetail) {
			this.alertDetail = alertDetail;
		}

		@Override
		protected void init(ParameterMap parameters) {
			this.setAlertName(parameters.get(INIT_ALERT_NAME));
			this.setAlertDetail(parameters.get(INIT_ALERT_DETAIL));
		}

		public String toString() {
			return "Alert Type " + this.getClass().getName() + " " + INIT_ALERT_NAME + " > " + this.alertName + " " + INIT_ALERT_DETAIL
					+ " > " + this.alertDetail;
		}
	}

	public static Notification fromParameterMap(ParameterMap parameterMap) {
		String notificationType = parameterMap.get("NotificationType");
		if (notificationType == null) {
			String types = " ";
			for (NotificationType type : NotificationType.values()) {
				types += type.name() + ",";
			}
			throw new RuntimeException("NotificationType must be defined as a valid type " + types);
		}

		NotificationType type = NotificationType.valueOf(notificationType);

		Notification notification = NotificationFactory.createNotification(type);

		notification.init(parameterMap);

		return notification;
	}

	protected abstract void init(ParameterMap parameters);

}
