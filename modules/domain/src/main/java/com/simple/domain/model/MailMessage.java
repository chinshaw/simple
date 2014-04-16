package com.simple.domain.model;
import java.util.List;


public class MailMessage {
	private List<String> toAddressList;
	private String subject;
	private String priority;	
	private String messageBody;
	private List<String> imagePaths;
		
	public List<String> getImagePaths() {
		return imagePaths;
	}
	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}
		
	public MailMessage  getMailMessageInstance(){
		return new MailMessage();
	}
	public List<String> getToAddressList() {
		return toAddressList;
	}
	public void setToAddressList(List<String> toAddressList) {
		this.toAddressList = toAddressList;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	
}
