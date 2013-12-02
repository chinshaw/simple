package com.simple.reporting;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import com.google.inject.Inject;
import com.simple.domain.AnalyticsTaskExecution;
import com.simple.domain.AnalyticsTaskMonitor;
import com.simple.domain.dao.AnalyticsTaskMonitorDao;
import com.simple.domain.metric.Metric;

/**
 * Class provides the complete factory alert generation with Quix case creation
 * and notifies the subscribed users
 * 
 * @author nallaraj
 * 
 */
public class ReportManager {
	private static final Logger logger = Logger.getLogger(ReportManager.class.getName());


	
	private final ReportNotificationManager notificationManager;
	
	
	@Inject
	public ReportManager(AnalyticsTaskMonitorDao monitorDao, ReportNotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public void sendReport(AnalyticsTaskExecution execution){
		try {
			String subject = "HP Virtual Factory Report for AnalyticsTaskExecution ID : " + execution.getId();
			notificationManager.sendNotification(subject, execution);
		}catch (MessagingException me) {
			logger.severe("ReportManager -> sendNotification throws sendNotification throws MessagingException ");
		}catch (NoUserSubscriptionException ne) {
			logger.info("ReportManager -> sendNotification throws NoUserSubscriptionException");
		}	    
	}
	
	

	/**
	 * This will return you AlertDefintions for a specific metric, it fist
	 * checks to see if the object has a parent
	 * @param metric
	 * @return
	 */
	protected List<AnalyticsTaskMonitor> getMetricMonitors(Metric metric) {
	   // Metric parent = metric.getParent();
	    List<AnalyticsTaskMonitor> alertDefinitions = null;
	    
	    // If this is not null then we have a 
	    // cloned metric.
	    
	    /*
	    if (parent != null) {
	        alertDefinitions = dao.findByMetric(metric);
	    } else {
	        alertDefinitions = dao.findByMetric(metric);
	    }
	    */
	    return alertDefinitions;
	}

	/**
	 * Helper method to generate current time in GMT
	 * @return Date
	 */
	public Date getCurrentGmtTime(){
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

		//Local time zone   
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

		Date gmtTime = null;
		//Time in GMT
		try {
			gmtTime = dateFormatLocal.parse( dateFormatGmt.format(new Date()) );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.info("Exception in parsing to GMT time"+gmtTime);
		}

		return gmtTime;
	}
}
