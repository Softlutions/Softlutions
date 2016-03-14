package com.cenfotec.dondeEs.controller;

import java.util.Date;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.ContractNotification;
import com.cenfotec.dondeEs.contracts.EventParticipantResponse;
import com.cenfotec.dondeEs.ejb.Auction;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.ejb.OfflineUser;
import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.logic.AES;
import com.cenfotec.dondeEs.pojo.ListSimplePOJO;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;
import com.cenfotec.dondeEs.services.ServiceContactInterface;
import com.cenfotec.dondeEs.services.ServiceInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/sendEmail")
public class SendEmailController {

	public static final String APP_DOMAIN = "http://localhost:8080";
	private static String subject;
	private static String text;
	@Autowired
	private EventParticipantServiceInterface eventParticipantServiceInterface;
	@Autowired
	private UserServiceInterface userserviceInterface;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private ServiceContactInterface serviceContactService;

	/**
	 * @author Antoni Ramirez Montano
	 * @param to parametro con el que se recibe la lista de correos
	 * @param eventId se recibe el id del evento para el cual han sido invitados
	 * @version 1.0
	 */
	@RequestMapping(value = "/sendEmailInvitation", method = RequestMethod.POST)
	public void sendEmailInvitation(@RequestBody ListSimplePOJO to, @QueryParam("eventId") int eventId) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		subject = "Invitacion a un evento";
		try {

			// To get the array of addresses
			for (String email : to.getListSimple()) {

				EventParticipantResponse response = new EventParticipantResponse();

				EventParticipant eventParticipant = new EventParticipant();
				eventParticipant.setEvent(new Event());
				eventParticipant.getEvent().setEventId(eventId);
				eventParticipant.setState((byte) 1);
				User user = userserviceInterface.findByEmail(email);

				if (user != null) {
					eventParticipant.setUser(user);
				} else {
					OfflineUser offlineUser = new OfflineUser();
					offlineUser.setEmail(email);
					eventParticipant.setOfflineUser(offlineUser);
				}
				eventParticipant.setInvitationDate(new Date());
				Boolean stateResponse = eventParticipantServiceInterface.saveParticipant(eventParticipant);

				if (stateResponse) {
					response.setCode(200);
				} else {
					response.setCodeMessage("Something is wrong");
				}

				text = "http://localhost:8080/dondeEs/app#/answerInvitation?eventId="
						+ AES.base64encode(String.valueOf(eventId)) + "&email=" + AES.base64encode(email)
						+ "&eventParticipantId="
						+ AES.base64encode(String.valueOf(eventParticipant.getEventParticipantId()));

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
	 * @author Ernesto Mendez A.
	 * @param auction subasta a la que se desea invitar
	 * @param to email del usuariod el servicio a invitar
	 * @param event evento al cual se crea la subasta
	 * @version 1.0
	 */
	public void sendAuctionInvitationEmail(Auction auction, String to, Event event) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		
		String subject = "Invitación a "+auction.getName();
		mailMessage.setSubject(subject);
		
		String msj = APP_DOMAIN+"/dondeEs/app#/getAllAuctionByEvent/?id="+event.getEventId();
		mailMessage.setText(msj);
		
		try{
			mailSender.send(mailMessage);
		}catch(Exception ae){
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
	@RequestMapping(value = "/sendEmailContractNotification", method = RequestMethod.POST)
	public void sendEmailContractNotification(@RequestBody ContractNotification contractNotification) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		subject = "Has sido contratado por un promotor";
		try {
			int eventId = contractNotification.getEvent().getEventId();
			int serviceId = contractNotification.getService().getServiceId();

			ServiceContact serviceContact = serviceContactService.getByServiceServiceIdAndEventEventId(
					contractNotification.getEvent().getEventId(), contractNotification.getService().getServiceId());
			
			String email = serviceInterface.getServiceById(serviceContact.getService().getServiceId()).getUser()
					.getEmail();
			text = "http://localhost:8080/dondeEs/app#/answerContract/?eventId="
					+ AES.base64encode(String.valueOf(eventId)) + "&serviceId="
					+ AES.base64encode(String.valueOf(serviceId));

			mailMessage.setTo(email);
			mailMessage.setText(text);
			mailMessage.setSubject(subject);
			mailSender.send(mailMessage);

		} catch (Exception ae) {
			ae.printStackTrace();
		}
	}

}
