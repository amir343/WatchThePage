package com.amirmoulavi.watchthatpage.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-20
 * @since 0.0.1
 *
 */

public class MailAuthenticator extends Authenticator {
	
	private String user;
	private String password;

	public MailAuthenticator(String user, String password){
		
		this.user = user;
		this.password = password;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {

		return new PasswordAuthentication(user, password);
	}

}
