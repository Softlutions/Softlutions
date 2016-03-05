package com.cenfotec.dondeEs.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.pojo.ListSimplePOJO;
@RestController
@RequestMapping(value = "rest/protected/sendEmail")
public class SendEmailController {

	private static String subject;
	private static String text;
	
	   @Autowired
	   private JavaMailSender mailSender;
	     
	@RequestMapping(value = "/sendEmailInvitation", method = RequestMethod.POST)
	public void sendEmailInvitation(@RequestBody ListSimplePOJO to, @QueryParam("eventId") int eventId) {
		
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		subject = "Invitacion a un evento";
		try {

			// To get the array of addresses
			for (String email : to.getListSimple()) {
				
				text = "http://localhost:8080/dondeEs/app#/answerInvitation?eventId="+ eventId+"&email="+email;
			
				mailMessage.setTo(email);
				mailMessage.setText(text);
				mailMessage.setSubject(subject);
				mailSender.send(mailMessage);
				
			}

			
		} catch (Exception ae) {
			ae.printStackTrace();
		} 
	}
	


}
