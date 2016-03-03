package com.cenfotec.dondeEs.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.QueryParam;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.pojo.ListSimplePOJO;
@RestController
@RequestMapping(value = "rest/protected/sendEmail")
public class SendEmailController {

	@RequestMapping(value = "/sendEmailInvitation", method = RequestMethod.POST)
	public void sendEmailInvitation(@RequestBody ListSimplePOJO to, @QueryParam("eventId") int eventId) {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";

		String from = "evaluacionescenfotec@gmail.com";
		String pass = "evaluacionescenfotec2015";

		props.put("mail.smtp.starttls.enable", "true");

		props.put("mail.smtp.ssl.trust", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		

		try {

			
			Transport transport = session.getTransport("smtp");

			// To get the array of addresses
			for (String email : to.getListSimple()) {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				
				InternetAddress internetAddress = new InternetAddress(email);
				message.addRecipient(Message.RecipientType.TO, internetAddress);
				message.setSubject("Invitacion a un evento");
				message.setText("http://localhost:8080/dondeEs/app#/answerInvitation?eventId="+ eventId+"&email="+email);
				transport.connect(host, from, pass);
				transport.sendMessage(message, message.getAllRecipients());

			}
			transport.close();

			
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
	


}
