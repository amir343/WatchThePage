package com.amirmoulavi.watchthatpage.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.amirmoulavi.watchthatpage.resource.ClassResourceLocator;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-19
 * @since 0.0.1
 *
 */

public class MailService {
	
	private static MailService instance = new MailService();
	public static MailService getInstance() {
		return instance;
	}
	
	private static  Logger log = Logger.getLogger(MailService.class);
	private String smtp_host = null;
	private String smtp_port = null;
	private String smtp_sending_user = null;
	private String smtp_support_user = null;
	private boolean smtp_debug = false;
	private boolean smtp_auth = false;
	private String smtp_auth_host = null;
	private String smtp_auth_port = null;
	private String smtp_auth_user = null;
	private String smtp_auth_password = null;
		
		
	private Properties props = new Properties();
	private String toEmail;

	public MailService() {

		InputStream is = ClassResourceLocator.getResourceAsStream("mail.properties");
		Properties bc = new Properties();
		try {
			bc.load(is);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		smtp_host = bc.getProperty("host");
		smtp_port = bc.getProperty("port");
		smtp_sending_user = bc.getProperty("sending_user");
		smtp_support_user = bc.getProperty("support_user");
		smtp_debug = "true".equals(bc.getProperty("debug"));

		smtp_auth_host = bc.getProperty("auth_host");
		smtp_auth_port = bc.getProperty("auth_port");
		smtp_auth_user = bc.getProperty("auth_user");
		smtp_auth_password = bc.getProperty("auth_password");
		toEmail = bc.getProperty("toEmail");

		if (null != bc.getProperty("authentication") && "true".equals(bc.getProperty("authentication"))) {
			smtp_auth = true;
		}

		if (smtp_auth) {
			props.put("mail.smtp.user", smtp_auth_user);
			props.put("mail.smtp.host", smtp_auth_host);
			props.put("mail.smtp.port", smtp_auth_port);
			props.put("mail.smtp.starttls.enable", "true");

			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", smtp_auth_port);
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtps.auth", "false");
			props.put("mail.smtp.quitwait", "false");

		} else {

			props.put("mail.smtp.host", smtp_host);
			props.put("mail.smtp.port", smtp_port);
			props.put("mail.smtp.quitwait", "false");
		}
	}
	
	public void mail(List<String> changedPages) throws MessagingException {
		StringBuilder sb = new StringBuilder();
		sb.append("The following pages are changed: \n\n\n");
		for (String page : changedPages ) {
			sb.append(page).append("\n");
		}
		
		sendHtmlTo("Page(s) changed!", sb.toString(), toEmail, "noreply@squace.com");
	}

	public String sendHtmlTo(String subject, String message, String toEmail,
			String fromEmail) throws MessagingException {
		String returnString = "Mail was successfully sent!";
		try {
			returnString = postMail(subject, message, toEmail, fromEmail, "text/html", new HashMap<String, String>());
		} catch (RuntimeException e) {
			log.error("Failed to send message " + message + " to "
							+ toEmail, e);
			log.error("Failed to send mail!", e);
		}

		return returnString;
	}

	private String postMail(String subject, String message, String toEmail,
			String replyTo, String contentType, Map<String, String> mailHeaders)
			throws MessagingException {

		String returnString = "Mail was sent!";
		Authenticator auth = null;
		Session session = null;

		if (smtp_auth) {
			auth = new MailAuthenticator(smtp_auth_user, smtp_auth_password);
		}
		session = Session.getInstance(props, auth);

		session.setDebug(smtp_debug);

		MimeMessage msg = new MimeMessage(session);

		for (String header : mailHeaders.keySet()) {
			msg.addHeader(header, mailHeaders.get(header));
		}

		msg.setSubject(subject, "UTF-8");
		msg.setContent(message, contentType + ";charset=UTF-8");
		msg.setFrom(new InternetAddress(smtp_sending_user));
		if (null != replyTo) {
			Address[] addresses = new Address[] { new InternetAddress(replyTo) };
			msg.setReplyTo(addresses);
		}
		msg.setRecipients(Message.RecipientType.TO,
				new InternetAddress[] { new InternetAddress(toEmail) });

		try {
			Transport.send(msg);
		} catch (MessagingException e) {
			log.error("Failed to send email to: " + toEmail, e);
			returnString = "Mail was sent, but with some problems. please contact tech!";
		}

		return returnString;
	}

}
