package com.simple.reporting;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import com.google.inject.Inject;
import com.simple.domain.AnalyticsTask;
import com.simple.domain.AnalyticsTaskExecution;
import com.simple.domain.MailMessage;
import com.simple.domain.Person;
import com.simple.domain.dao.AnalyticsTaskDao;
import com.simple.original.api.analytics.IPerson;


public class ReportNotificationManager {

	private static final Logger logger = Logger.getLogger(ReportNotificationManager.class.getName());

	private final AnalyticsTaskDao taskDao;
	
	@Inject
	private SmtpUtils smtpUtils;
	
	@Inject
	public ReportNotificationManager(AnalyticsTaskDao taskDao){
		this.taskDao = taskDao;
	}

	public void sendNotification(String subject, AnalyticsTaskExecution taskExecution) throws MessagingException, NoUserSubscriptionException{

		MailMessage mailMessagePDF = new MailMessage();

		List<String> addressList = getAddressDistributionList(taskExecution.getAnalyticsTask().getId());
		// Set toAddressList
		mailMessagePDF.setToAddressList(addressList);
		//String subject = "HP Virtual Factory Report for AnalyticsTaskExecution ID : " + taskExecution.getId();

		mailMessagePDF.setSubject(subject);
		logger.fine("ReportNotificationManager -> Subject:  "+ mailMessagePDF.getSubject());

		//Set Message content
		String messageContent = "P_CHART for Fixed_Subgroup_With_Phases has been executed.";

		mailMessagePDF.setMessageBody(messageContent);
		logger.fine("PDF Message Body():  "+ mailMessagePDF.getMessageBody());
		String pdfFileName = "TEMPORARY STUPIDITY";
		String pdfFilePath = "WTF";

		smtpUtils.sendPDFInMail(mailMessagePDF, pdfFilePath,pdfFileName);
	}
	
	/**
	 * @param reportTask
	 * @return
	 * Method used to get distribution List for Email to send mail 
	 * @throws NoUserSubscriptionException
	 */
	private List<String> getAddressDistributionList(Long reportTaskId) throws NoUserSubscriptionException{
		AnalyticsTask task  = taskDao.find(new Long(reportTaskId));
		
		List<String> mailAddressList = new ArrayList<String>();
		List<Person> userList = task.getSubscribers();

		if(userList!= null && userList.size() > 0){
			for(IPerson user : userList){
				if(user.getPreferences().getEmailFlag()){
					mailAddressList.add(user.getPreferences().getSubscriberMailId());
				}
			}
		}else {
			throw new NoUserSubscriptionException("No user has Subscribed for this Report :"+task.getId());
		}
		return  mailAddressList;
	}
}
