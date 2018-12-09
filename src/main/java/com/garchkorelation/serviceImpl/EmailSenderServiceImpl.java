package com.garchkorelation.serviceImpl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garchkorelation.model.User;
import com.garchkorelation.service.EmailSenderService;
import com.garchkorelation.service.UserService;
import com.garchkorelation.util.HmacSha1Signature;
import com.garchkorelation.util.StaticVar;

@Service("emailSenderService")
public class EmailSenderServiceImpl implements EmailSenderService {

	@Autowired
	private UserService userService;

	private void sendEmail(String host, String port, final String userName, final String password, String toAddress, String subject, String message) throws AddressException, MessagingException {

		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};

		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		// set plain text message
		msg.setContent(message, "text/html");

		// sends the e-mail
		Transport.send(msg);

	}

	@Override
	public void sendEmail(String mailTo, String subject, String message) {
		// SMTP server information
		String host = "smtp.gmail.com";
		String port = "587";
		String mailFrom = "testmodiphius@gmail.com";
		String password = "qwertyuI1@";

		try {
			sendEmail(host, port, mailFrom, password, mailTo, subject, message);
			System.out.println("Email sent.");
		} catch (Exception ex) {
			System.out.println("Failed to sent email.");
			ex.printStackTrace();
		}
	}

	@Override
	public void sendVerification() {
		try {
			User currentUser = userService.getCurrentUser();
			String value = HmacSha1Signature.getValueFromUser(currentUser);
			String hmac = HmacSha1Signature.calculateRFC2104HMAC(value);
			String confirmURL = StaticVar.CONTEXT_PATH + "/rest/user/email/" + hmac + "/confirm";
			sendEmail(currentUser.getEmail(), "verification", confirmURL);
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
