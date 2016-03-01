package com.cenfotec.dondeEs.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.EventParticipantResponse;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ListSimplePOJO;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/eventParticipant")
public class EventParticipantController {
	@Autowired
	private EventParticipantServiceInterface eventParticipantServiceInterface;

	@RequestMapping(value = "/getAllEventParticipantByEvent/{id}", method = RequestMethod.GET)
	public EventParticipantResponse getAllEventParticipantByEvent(@PathVariable("id") int id) {
		EventParticipantResponse response = new EventParticipantResponse();

		return response;
	}

	@RequestMapping(value = "/sendEmailInvitation", method = RequestMethod.POST)
	public void sendEmailInvitation(@RequestBody ListSimplePOJO listSimple) {
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
		MimeMessage message = new MimeMessage(session);

		try {

			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[listSimple.getListSimple().size()];

			// To get the array of addresses
			for (int i = 0; i < listSimple.getListSimple().size(); i++) {
				toAddress[i] = new InternetAddress(listSimple.getListSimple().get(i));
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject("Test");
			message.setText("Hola");

			Transport transport = session.getTransport("smtp");

			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			System.out.println("Sirve");
			transport.close();

		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}

	}

	@RequestMapping(value = "/createEventParticipant/{id}", method = RequestMethod.POST)
	public EventParticipantResponse createEventParticipant(@PathVariable("id") int id, byte state, User user)
			throws ParseException {

		EventParticipantResponse response = new EventParticipantResponse();

		EventParticipant eventParticipant = new EventParticipant();
		eventParticipant.setEvent(new Event());
		eventParticipant.getEvent().setEventId(id);
		eventParticipant.setState(state);
		if (user != null) {
			eventParticipant.setUser(user);
		}

		Boolean stateResponse = eventParticipantServiceInterface.saveParticipant(eventParticipant);

		if (stateResponse) {
			response.setCode(200);
		} else {
			response.setCodeMessage("Something is wrong");
		}
		return response;
	}
}
