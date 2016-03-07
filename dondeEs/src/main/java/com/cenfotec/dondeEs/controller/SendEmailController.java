package com.cenfotec.dondeEs.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.pojo.ListSimplePOJO;
import com.cenfotec.dondeEs.services.ServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/sendEmail")
public class SendEmailController {
	@Autowired
	private ServiceInterface serviceInterface;

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

	/**
	 * @author Alejandro Bermúdez Vargas
	 * @exception AddressException no se encuentra la direccion de correo
	 * @exception MessagingException No encuentra el server.
	 * @param id, el id del servicio que se contrato
	 * @version 1.0
	 */
	@RequestMapping(value = "/sendEmailContractNotification/{serviceId}", method = RequestMethod.GET)
	public void sendEmailContractNotification(@PathVariable("serviceId") int id) {
		generalEmail();
		Session session = Session.getDefaultInstance(props);
		subject = "Solicitud de contratación!";
		try {
			Transport transport = session.getTransport("smtp");
			String email = serviceInterface.getServiceById(id).getUser().getEmail();
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			text = "Link x" + id + "&email=" + email;
			InternetAddress internetAddress = new InternetAddress(email);
			message.addRecipient(Message.RecipientType.TO, internetAddress);
			message.setSubject(subject);
			message.setText(text);
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
	
	/***
	 * @author Enmanuel García González
	 * @param id
	 */
	@RequestMapping(value = "/sendEmailCancelEventNotification/{serviceId}", method = RequestMethod.GET)
	public void sendEmailCancelEventNotification(@PathVariable("serviceId") int id) {
		generalEmail();
		Session session = Session.getDefaultInstance(props);
		subject = "Solicitud de contratación!";
		try {
			Transport transport = session.getTransport("smtp");
			String email = serviceInterface.getServiceById(id).getUser().getEmail();
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			text = "Link x" + id + "&email=" + email;
			InternetAddress internetAddress = new InternetAddress(email);
			message.addRecipient(Message.RecipientType.TO, internetAddress);
			message.setSubject(subject);
			message.setText(text);
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

}
