package com.execution.service.monitoring_execution_service.utility;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender implements NotificationSender {

	private String username;
	private String password;
	private String host;
	private String port;
	private String auth;
	private String tlsEnable;
	private String from;

	public EmailSender() throws Exception {
		this.username = PropertyReader.readProperty("username");
		this.password = PropertyReader.readProperty("password");
		this.host = PropertyReader.readProperty("smtp_host");
		this.port = PropertyReader.readProperty("smtp_port");
		this.auth = PropertyReader.readProperty("smtp_auth");
		this.tlsEnable = PropertyReader.readProperty("smtp_starttls_enable");
		this.from = PropertyReader.readProperty("from");
	}

	@Override
	public boolean sendNotification(String target, String subject, String text, String fileName, String filePath){
		
		boolean res= false;
		try{
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port",port);
			props.put("mail.smtp.starttls.enable",tlsEnable);
			props.put("mail.smtp.auth",auth);
			
		      Session session = Session.getInstance(props,
		    	         new javax.mail.Authenticator() {
		    	            protected PasswordAuthentication getPasswordAuthentication() {
		    	               return new PasswordAuthentication(username, password);
		    	            }
		      });
		      // Create a default MimeMessage object.
		      Message message = new MimeMessage(session);
		      // Set From: header field of the header.
		      message.setFrom(new InternetAddress(from));
		      // Set To: header field of the header.
		      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(target));
		      // Set Subject: header field
		      message.setSubject(subject);
		      // Create the message part
		      BodyPart messageBodyPart = new MimeBodyPart();
		      // Now set the actual message
		      messageBodyPart.setText(text);
		      // Create a multipar message
		      Multipart multipart = new MimeMultipart();
		      // Set text message part
		      multipart.addBodyPart(messageBodyPart);
		      // Part two is attachment
		      messageBodyPart = new MimeBodyPart();
		      DataSource source = new FileDataSource(filePath);
		      messageBodyPart.setDataHandler(new DataHandler(source));
		      messageBodyPart.setFileName(fileName);
		      multipart.addBodyPart(messageBodyPart);
		      
		      // Send the complete message parts
		      message.setContent(multipart);
		      
		      // Send message
		      Transport.send(message);
		      res=true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	      return res;
	}

}
