package com.simple.reporting;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Class used to send Mail as HTML format and Text format
 * 
 * @author hemantha
 * 
 */
public class SmtpUtils {

	private static final Logger logger = Logger.getLogger(SmtpUtils.class.getName());

	@Inject
	private static Properties mailProperties;
	
	@Named("com.simple.reporting.plots.dir")
	private static String plotsAbsoluteDirectory;

	
	private SmtpUtils() {
	}

	/**
	 * @param mailMessage
	 * @return Method to send Mail as HTML format
	 * @throws MessagingException
	 */
	public boolean sendHTMLMail(MailMessage mailMessage) throws MessagingException {
		Session mailSession = Session.getInstance(mailProperties);
		MimeMessage message = new MimeMessage(mailSession);
		MimeMultipart multipart = new MimeMultipart("related");

		// Add html
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(mailMessage.getMessageBody(), "text/html");
		multipart.addBodyPart(messageBodyPart);

		// Add Image
		messageBodyPart = new MimeBodyPart();
		if (mailMessage.getImagePaths() != null) {
			for (String image_path : mailMessage.getImagePaths()) {
				String absoluteImagePath = plotsAbsoluteDirectory + image_path;
				DataSource fds = new FileDataSource(absoluteImagePath);
				messageBodyPart.setDataHandler(new DataHandler(fds));
				messageBodyPart.setFileName(absoluteImagePath);
				messageBodyPart.setHeader("Content-ID", "<image>");
				multipart.addBodyPart(messageBodyPart);
			}
		}

		if (mailMessage.getPriority() != null) {
			message.addHeader("X-Priority", mailMessage.getPriority());
		}
		// Set Message data
		message.setContent(multipart);
		message.setSubject(mailMessage.getSubject());
		//message.setFrom(new InternetAddress(fromAddress));
		for (String toAddress : mailMessage.getToAddressList()) {
		    logger.info("Sending to address -> " + toAddress);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
		}
		// Send Message
		Transport.send(message);
		logger.info("SendMailService -> sendHtmlMail()-> Mail Sent Successfully @ " + new Date());
		return true;
	}

	/**
	 * @param mailMessage
	 * @return Method to send Mail as Text format
	 * @throws MessagingException
	 */
	public static boolean sendTextMail(MailMessage mailMessage) throws MessagingException {

		Session mailSession = Session.getInstance(mailProperties);
		Message message = new MimeMessage(mailSession);
		// Set Message data
		if (mailMessage.getPriority() != null) {
			message.addHeader("X-Priority", mailMessage.getPriority());
		}
		message.setContent(mailMessage.getMessageBody(), "text/plain");
		message.setSubject(mailMessage.getSubject());
		// message.setFrom(new InternetAddress(fromAddress));
		for (String toAddress : mailMessage.getToAddressList()) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
		}
		// Send Message
		Transport.send(message);
		logger.info("SendMailService -> sendTextMail()-> Mail Sent Successfully @ " + new Date());
		return true;
	}

	public static boolean sendPDFInMail(MailMessage mailMessage, String pdfFileAbsolutePath, String pdfFileName) throws MessagingException {
		Session mailSession = Session.getInstance(mailProperties);
		MimeMessage message = new MimeMessage(mailSession);
		MimeMultipart multipart = new MimeMultipart("related");

		// Add html
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(mailMessage.getMessageBody(), "text/html");
		multipart.addBodyPart(messageBodyPart);

		// Add PDF

		messageBodyPart = new MimeBodyPart();
		DataSource fds = new FileDataSource(pdfFileAbsolutePath);
		messageBodyPart.setDataHandler(new DataHandler(fds));
		messageBodyPart.setFileName(pdfFileName);
		multipart.addBodyPart(messageBodyPart);

		// Set Message data
		message.setContent(multipart);
		message.setSubject(mailMessage.getSubject());
		// message.setFrom(new InternetAddress(fromAddress));
		for (String toAddress : mailMessage.getToAddressList()) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
		}

		// Send Message
		Transport.send(message);
		
		logger.info("SendMailService -> sendPDFInMail()-> Mail Sent Successfully @ " + new Date());
		return true;
	}
	
	@Inject
	public void setMailProperties(Properties mailProperties) {
		this.mailProperties = mailProperties;
	}

}
