package com.cenfotec.dondeEs.controller;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.BaseResponse;
import com.cenfotec.dondeEs.contracts.EventParticipantResponse;
import com.cenfotec.dondeEs.contracts.MessageRequest;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.ejb.OfflineUser;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ListSimplePOJO;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/sendEmail")
public class SendEmailController {

	private static String subject;
	private static String text;
	@Autowired
	private EventParticipantServiceInterface eventParticipantServiceInterface;
	@Autowired
	private UserServiceInterface userserviceInterface;
	@Autowired
	private JavaMailSender mailSender;

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
				// eventParticipant.getOfflineUser().setEmail(email);
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

				text = "http://localhost:8080/dondeEs/app#/answerInvitation?eventId=" + eventId + "&email=" + email
						+ "&eventParticipantId=" + eventParticipant.getEventParticipantId();
				
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
	 * @exception AddressException
	 *                no se encuentra la direccion de correo
	 * @exception MessagingException
	 *                No encuentra el server.
	 * @param id,
	 *            el id del servicio que se contrato
	 * @version 1.0
	 */
	/*
	 * @RequestMapping(value = "/sendEmailContractNotification", method =
	 * RequestMethod.POST) public void
	 * sendEmailContractNotification(@RequestBody ContractNotification
	 * contractNotification) { generalEmail(); Session session =
	 * Session.getDefaultInstance(props); subject = "Solicitud de contratación!"
	 * ; try { int eventId = contractNotification.getEvent().getEventId(); int
	 * serviceId = contractNotification.getService().getServiceId();
	 * 
	 * Transport transport = session.getTransport("smtp"); // Contact service
	 * ServiceContact serviceContact =
	 * serviceContactService.getByServiceServiceIdAndEventEventId(
	 * contractNotification.getEvent().getEventId(),
	 * contractNotification.getService().getServiceId()); // Email del usuario
	 * String email =
	 * serviceInterface.getServiceById(serviceContact.getService().getServiceId(
	 * )).getUser() .getEmail(); MimeMessage message = new MimeMessage(session);
	 * message.setFrom(new InternetAddress(from)); text =
	 * "http://localhost:8080/dondeEs/app#/answerInvitation/?eventId=" +
	 * AES.base64encode(String.valueOf(eventId)) + "&serviceId=" +
	 * AES.base64encode(String.valueOf(serviceId));
	 * 
	 * InternetAddress internetAddress = new InternetAddress(email);
	 * message.addRecipient(Message.RecipientType.TO, internetAddress);
	 * message.setSubject(subject); message.setText(text);
	 * transport.connect(host, from, pass); transport.sendMessage(message,
	 * message.getAllRecipients()); transport.close(); } catch (AddressException
	 * ae) { ae.printStackTrace(); } catch (MessagingException me) {
	 * me.printStackTrace(); } }
	 * 
	 * /***
	 * 
	 * @author Enmanuel García González
	 * 
	 * @param id /*@RequestMapping(value =
	 * "/sendEmailCancelEventNotification/{serviceId}", method =
	 * RequestMethod.GET) public void
	 * sendEmailCancelEventNotification(@PathVariable("serviceId") int id) {
	 * generalEmail(); Session session = Session.getDefaultInstance(props);
	 * subject = "Solicitud de contratación!"; try { Transport transport =
	 * session.getTransport("smtp"); String email =
	 * serviceInterface.getServiceById(id).getUser().getEmail(); MimeMessage
	 * message = new MimeMessage(session); message.setFrom(new
	 * InternetAddress(from)); text = "Link x" + id + "&email=" + email;
	 * InternetAddress internetAddress = new InternetAddress(email);
	 * message.addRecipient(Message.RecipientType.TO, internetAddress);
	 * message.setSubject(subject); message.setText(text);
	 * transport.connect(host, from, pass); transport.sendMessage(message,
	 * message.getAllRecipients()); transport.close(); } catch (AddressException
	 * ae) { ae.printStackTrace(); } catch (MessagingException me) {
	 * me.printStackTrace(); } }
	 */
	
	/***
	 * Se envía un mensaje de parte de un usuario de la aplicación al correo de los encargados
	 * del software.
	 * @author Enmanuel García González
	 * @param message
	 * @version 1.0
	 */
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public void sendMessage(@RequestBody MessageRequest message) {
		BaseResponse response = new BaseResponse();
		
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			subject = "Mensaje del usuario";
			text = "Ha recibido un mensaje del usuario: " + message.getUserName() + ", " +
						"correo: " + message.getUserEmail() + ", " +
						"mensaje: " + message.getMessage();
				
			mailMessage.setTo("egarciag@ucenfotec.ac.cr"); // correo de prueba
			mailMessage.setText(text);
			mailMessage.setSubject(subject);
			mailSender.send(mailMessage);
			
			response.setCode(200);
		} catch (Exception ae) {
			response.setCode(500);
			response.setErrorMessage("Ha ocurrido un error interno.");
			ae.printStackTrace();
		}
	}

}
