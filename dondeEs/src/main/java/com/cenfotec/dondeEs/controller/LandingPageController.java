package com.cenfotec.dondeEs.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.contracts.CommentResponse;
import com.cenfotec.dondeEs.contracts.EventImageResponse;
import com.cenfotec.dondeEs.contracts.EventParticipantResponse;
import com.cenfotec.dondeEs.contracts.EventResponse;
import com.cenfotec.dondeEs.contracts.ServiceContactRequest;
import com.cenfotec.dondeEs.contracts.ServiceContactResponse;
import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.ejb.OfflineUser;
import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.logic.AES;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.pojo.ListSimplePOJO;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;
import com.cenfotec.dondeEs.services.CommentServiceInterface;
import com.cenfotec.dondeEs.services.EventImageServiceInterface;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;
import com.cenfotec.dondeEs.services.EventServiceInterface;
import com.cenfotec.dondeEs.services.ServiceContactInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;
import com.cenfotec.dondeEs.utils.Utils;

/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value = "rest/landing")
public class LandingPageController {

	@Autowired
	private EventParticipantServiceInterface eventParticipantServiceInterface;
	@Autowired
	private EventImageServiceInterface eventImageServiceInterface;
	@Autowired
	private CommentServiceInterface commentServiceInterface;
	@Autowired
	private EventServiceInterface eventServiceInterface;
	@Autowired
	private UserServiceInterface userServiceInterface;
	@Autowired
	private ServiceContactInterface serviceContactInterface;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private UserServiceInterface userserviceInterface;
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * @author Ernesto Mendez A.
	 * @param eventParticipantId
	 *            id del participante del evento asociado
	 * @param file
	 *            archivo de imagen a subir
	 * @return si la operacion fue efectiva o no
	 * @version 1.0
	 */
	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	public EventImageResponse saveImage(@RequestParam("eventParticipantId") int eventParticipantId,
			@RequestParam("file") MultipartFile file) {
		EventImageResponse response = new EventImageResponse();

		if (eventImageServiceInterface.saveImage(eventParticipantId, file)) {
			response.setCode(200);
			response.setCodeMessage("Success");
		} else {
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}

		return response;
	}

	/**
	 * @author Ernesto Mendez A.
	 * @param eventId
	 *            id del evento del cual se desea objetener las imagenes
	 *            asociadas
	 * @return Lista de imagenes del evento
	 * @version 1.0
	 */
	@RequestMapping(value = "/getImagesByEventId/{eventId}", method = RequestMethod.GET)
	public EventImageResponse getAllByUser(@PathVariable("eventId") int eventId) {
		EventImageResponse response = new EventImageResponse();
		response.setCode(200);
		response.setCodeMessage("Success");
		response.setImages(eventImageServiceInterface.getAllByEventId(eventId));
		return response;
	}

	/**
	 * @author Juan Carlos Sánchez G.
	 * @param eventId
	 *            Peticion que contiene la información del comentario por crear.
	 * @return Respuesta del servidor de la petición.
	 * @version 1.0
	 */
	@RequestMapping(value = "/getCommentsByEvent/{eventId}", method = RequestMethod.GET)
	public CommentResponse getCommentsByEvent(@PathVariable("eventId") int eventId) {
		CommentResponse response = new CommentResponse();
		response.setCommentList(commentServiceInterface.getCommentsByEvent(eventId));

		return response;
	}

	/**
	 * @author Juan Carlos Sánchez G. (Autor)
	 * @author Ernesto Mendez A. (Subir imagen)
	 * @param participantId
	 *            id del participante que comenta
	 * @param content
	 *            texto del comentario
	 * @param file
	 *            (opcional) archivo de si comenta con una imagen
	 * @return Respuesta del servidor de la petición.
	 * @version 1.0
	 */
	@RequestMapping(value = "/saveComment", method = RequestMethod.POST)
	public CommentResponse saveComment(@RequestParam("participantId") int participantId,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		CommentResponse response = new CommentResponse();

		Comment comment = new Comment();
		comment.setDate(new Date());
		comment.setContent(content);

		EventParticipant participant = eventParticipantServiceInterface.findById(participantId);
		comment.setEventParticipant(participant);

		if (participant != null) {
			if (commentServiceInterface.saveComment(comment, file)) {
				response.setCode(200);
				response.setCodeMessage("Succesful");
			} else {
				response.setCode(500);
				response.setCodeMessage("Internal error");
			}
		} else {
			response.setCode(404);
			response.setCodeMessage("Participant not found");
		}

		return response;
	}

	/**
	 * @param commentId
	 *            id del comentario a eliminar
	 * @return si la operacion fue exitosa
	 * @version 1.0
	 */
	@RequestMapping(value = "/deleteComment/{commentId}", method = RequestMethod.GET)
	public CommentResponse deleteComment(@PathVariable int commentId) {
		CommentResponse response = new CommentResponse();

		if (commentServiceInterface.deleteComment(commentId)) {
			response.setCode(200);
			response.setCodeMessage("Success");
		} else {
			response.setCode(404);
			response.setCodeMessage("Comment not found");
		}

		return response;
	}

	/**
	 * @author Ernesto Mendez A.
	 * @param imageId
	 *            id de la imagen a eliminar
	 * @return si la operacion fue exitosa
	 * @version 1.0
	 */
	@RequestMapping(value = "/deleteImage/{imageId}", method = RequestMethod.GET)
	public EventImageResponse deleteImage(@PathVariable int imageId) {
		EventImageResponse response = new EventImageResponse();

		if (eventImageServiceInterface.deleteImage(imageId)) {
			response.setCode(200);
			response.setCodeMessage("Success");
		} else {
			response.setCode(404);
			response.setCodeMessage("Image not found");
		}

		return response;
	}

	/**
	 * @Author Juan Carlos Sánchez G.
	 * @param idEvent Id del evento del que se listarán los participantes.
	 * @return response Respuesta del servidor de la petición que incluye la lista de participantes del evento.
	 * @version 1.0
	 */
	@RequestMapping(value ="/getAllEventParticipants/{idEvent}", method = RequestMethod.GET)
	public EventParticipantResponse getAllEventParticipants(@PathVariable("idEvent") int idEvent){
		EventParticipantResponse response = new EventParticipantResponse();
		response.setEventParticipantsList(eventParticipantServiceInterface.getAllEventParticipants(idEvent));
		return response;
	}
	
	/**
	 * @author Ernesto Mendez A.
	 * @param participantId id del usuario a bloquear para este evento
	 * @param state nuevo estado del participante
	 * @return si la operacion fue exitosa
	 * @version 1.0
	 */
	@RequestMapping(value = "/participantState/{participantId}", method = RequestMethod.GET)
	public EventImageResponse participantState(@PathVariable int participantId, @RequestParam("state") byte state){
		EventImageResponse response = new EventImageResponse();
		
		if(eventParticipantServiceInterface.participantState(participantId, state)){
			response.setCode(200);
			response.setCodeMessage("Success");
		} else {
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}

		return response;
	}
	
	/**
	 * Consulta cualquier evento por su id.
	 * @author Enmanuel Garcia Gonzalez 
	 * @param eventId id del evento a consultar
	 * @return evento consultado
	 * @version 1.0
	 */
	@RequestMapping(value = "/getWhateverEventById/{eventId}", method = RequestMethod.GET)
	public EventResponse getWhateverEventById(@PathVariable("eventId") int eventId) {
		EventResponse response = new EventResponse();
		EventPOJO event = eventServiceInterface.eventById(eventId);
		
		if(event != null){
			response.setEventPOJO(event);
			response.setCode(200);
		}else{
			response.setCode(404);
			response.setCodeMessage("El evento no a sido publicado o no existe");
		}
		
		return response;
	}
	
	/**
	 * Consultar cualquier evento por su id solo y solo si este ya esta publicado
	 * @author Ernesto Mendez A.
	 * @param eventId id del evento a consultar
	 * @return evento consultado
	 * @version 1.0
	 */
	@RequestMapping(value = "/getEventById/{eventId}", method = RequestMethod.GET)
	public EventResponse getEventById(@PathVariable("eventId") int eventId) {
		EventResponse response = new EventResponse();
		EventPOJO event = eventServiceInterface.eventById(eventId);
		
		if(event != null){
			if(event.getState() == 3){
				response.setEventPOJO(event);
				response.setCode(200);
			}else{
				response.setCode(400);
				response.setCodeMessage("El evento no a sido publicado aún");
			}
		}else{
			response.setCode(404);
			response.setCodeMessage("El evento no a sido publicado o no existe");
		}
		
		return response;
	}

	/**
	 * @autor Ernesto Mendez A.
	 * @param userId
	 *            id del usuario logueado
	 * @param eventId
	 *            evento en el que participa
	 * @return respuesta del servidor
	 * @version 1.0
	 */
	@RequestMapping(value = "/getEventParticipantByUserAndEvent", method = RequestMethod.GET)
	public EventParticipantResponse getEventParticipantByUserAndEvent(@RequestParam("userId") int userId,
			@RequestParam("eventId") int eventId) {
		EventParticipantResponse response = new EventParticipantResponse();
		EventParticipantPOJO participant = eventParticipantServiceInterface.findByUserAndEvent(userId, eventId);

		if (participant != null) {
			response.setCode(200);
			response.setCodeMessage("success");
			response.setEventParticipant(participant);
		} else {
			response.setCode(404);
			response.setCodeMessage("Participant not found!");
		}

		return response;
	}

	/**
	 * @author Ernesto Mendez A.
	 * @param userId id del usuario en sesion
	 * @param eventId  id del evento
	 * @return id del nuevo participante
	 */
	@RequestMapping(value = "/createParticipant", method = RequestMethod.GET)
	public EventParticipantResponse createParticipant(@RequestParam("userId") int userId,
			@RequestParam("eventId") int eventId) {
		EventParticipantResponse response = new EventParticipantResponse();
		EventParticipantPOJO participant = eventParticipantServiceInterface.findByUserAndEvent(userId, eventId);

		if (participant != null) {
			response.setCode(202);
			response.setCodeMessage("You already accepted");
			response.setEventParticipant(participant);
		} else {
			EventParticipant eventParticipant = new EventParticipant();
			eventParticipant.setUser(userServiceInterface.findById(userId));
			eventParticipant.setEvent(eventServiceInterface.getEventById(eventId));
			eventParticipant.setState((byte) 2);

			int nparticipantId = eventParticipantServiceInterface.createParticipant(eventParticipant);

			if (nparticipantId == 0) {
				response.setCode(404);
				response.setCodeMessage("User or event not found!");
			} else {
				participant = eventParticipantServiceInterface.findByUserAndEvent(userId, eventId);
				
				response.setCode(200);
				response.setCodeMessage("Success");
				response.setEventParticipant(participant);
			}
		}

		return response;
	}

	/**
	 * @author Antoni Ramirez Montano
	 * @param nameUser
	 *            criterio a consultar
	 * @param namePlace
	 *            criterio a consultar
	 * @param name
	 *            criterio a consultar
	 * @return lista de eventos basados en los criterios
	 */
	@RequestMapping(value = "/getEventByParams/{nameUser}/{name}/{namePlace}", method = RequestMethod.GET)
	public EventResponse getEventByParams(@PathVariable("nameUser") String nameUser, @PathVariable("name") String name,
			@PathVariable("namePlace") String namePlace) {
		EventResponse e = new EventResponse();
		e.setEventList(eventServiceInterface.getAllByParam(nameUser, name, namePlace, (byte) 3));
		e.setCode(200);
		return e;
	}

	/**
	 * @author Enmanuel García González
	 * @return lista de eventos publicos
	 * @version 1.0
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getAllEventPublish", method = RequestMethod.GET)
	public EventResponse getAll() {
		EventResponse response = new EventResponse();

		try {
			response.setCode(200);
			response.setCodeMessage("eventsPublish fetch success");
			response.setEventList(eventServiceInterface.getAllEventPublish());

		} catch (Exception e) {
			response.setCode(500);
			response.setCodeMessage(e.toString());
			e.printStackTrace();

		} finally {
			return response;
		}
	}

	/**
	 * @author Ernesto Mendez A.
	 * @param top cantidad de items que tendra la lista top
	 * @return lista con los eventos con mas participantes
	 * @version 1.0
	 */
	@RequestMapping(value = "/getTopEvents/{top}", method = RequestMethod.GET)
	public EventResponse getTopEvents(@PathVariable("top") int top) {
		EventResponse response = new EventResponse();
		List<EventPOJO> list = eventServiceInterface.getTopEventsByParticipants(top);
		
		response.setCode(200);
		response.setCodeMessage("top events fetch success");
		response.setEventList(list);

		return response;
	}

	/***
	 * Devuelve un evento cuando le envian el id encriptado
	 * 
	 * @author Alejandro bermudez Vargas
	 * @param eventRequest
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(value = "/getEventByEncryptId/{idEvent}", method = RequestMethod.GET)
	public EventResponse getEventByEncryptId(@PathVariable("idEvent") String id) {
		EventResponse response = new EventResponse();
		int eventId = Integer.parseInt(AES.base64decode(id));
		if (eventId != 0) {
			response.setEventPOJO(eventServiceInterface.eventById(eventId));
			response.getEventPOJO().setPrivate_((byte) 1);
			response.getEventPOJO().setState((byte) 1);
			response.getEventPOJO().setPublishDate(new Date());
			response.setCode(200);
		} else {
			response.setCode(400);
			response.setCodeMessage("Something is wrong");
		}
		return response;
	}

	/**
	 * @author Antoni Ramirez Montano
	 * @param id
	 *            del participante a modificar
	 * @param state
	 *            se recibe la respuesta si va asistir
	 * @param comment
	 *            espacio para agregar algun mensaje con respecto al evento
	 * @return retorna el response que tiene el estado del url
	 * @throws ParseException
	 * @version 1.0
	 */
	@RequestMapping(value = "/updateEventParticipant/{id}", method = RequestMethod.PUT)
	public EventParticipantResponse updateEventParticipant(@PathVariable("id") String id,
			@QueryParam("state") byte state, @QueryParam("comment") String comment) throws ParseException {
		EventParticipantResponse response = new EventParticipantResponse();

		int idParticipant = Integer.parseInt(AES.base64decode(id));

		EventParticipant eventParticipant = eventParticipantServiceInterface.findById(idParticipant);

		eventParticipant.setState(state);
		// eventParticipant.getOfflineUser().setEmail(email);

		Comment ncomment = new Comment();
		ncomment.setContent(comment);
		ncomment.setDate(new Date());
		ncomment.setEventParticipant(eventParticipant);

		Boolean stateComment = commentServiceInterface.saveComment(ncomment);
		if (stateComment) {
			response.setCode(200);
		}

		Boolean stateResponse = eventParticipantServiceInterface.saveParticipant(eventParticipant);

		if (stateResponse) {
			response.setCode(200);
		} else {
			response.setCodeMessage("Something is wrong");
		}
		return response;
	}
	
	/**
	 * @author Ernesto Mendez A.
	 * @param promoterId id del promotor a consultar si tiene contratos
	 * @return lista de contratos pendientes de responder del promotor
	 * @version 1.0
	 */
	@RequestMapping(value = "/getContractsLeftByPromoter", method = RequestMethod.GET)
	public ServiceContactResponse getContractsLeftByPromoter(@RequestParam("promoterId") int promoterId) {
		ServiceContactResponse response = new ServiceContactResponse();
		
		List<ServiceContactPOJO> serviceContacts = serviceContactInterface.getContractsLeftByPromoter(promoterId);
		
		if(serviceContacts.size() > 0){
			response.setCode(200);
			response.setCodeMessage("Success");
			response.setListContracts(serviceContacts);
		}else{
			response.setCode(404);
			response.setCodeMessage("This promoter doesn't have any contract");
		}
		
		return response;
	}
	
	/**
	 * @Author Alejandro Bermudez Vargas
	 * @param ServiceContactRequest serviceContactRequest
	 * @return Retonrna el resultado de dicha respuesta
	 * @version 1.0
	 */
	@RequestMapping(value = "/getServiceContact", method = RequestMethod.POST)
	public ServiceContactResponse getServiceContact(@RequestBody ServiceContactRequest serviceContactRequest) {
		String streventId = AES.base64decode(serviceContactRequest.getEventId());
		String strserviceId = AES.base64decode(serviceContactRequest.getServiceId());
		
		ServiceContactResponse response = new ServiceContactResponse();
		ServiceContact serviceContact = serviceContactInterface
				.getByServiceServiceIdAndEventEventId(Integer.parseInt(strserviceId), Integer.parseInt(streventId));
		
		if(serviceContactRequest.getLoggedUserId() == serviceContact.getService().getUser().getUserId()){
			if (serviceContact.getState() == 0) {
				serviceContact.setState(serviceContactRequest.getState());
				response.setCode(200);
				response.setCodeMessage("Tienes una solicitud pendiente");
			} else if (serviceContact.getState() == 2) {
				response.setCode(201);
				response.setCodeMessage("Ya confirmaste!");
			} else if (serviceContact.getState() == 1) {
				response.setCode(202);
				response.setCodeMessage("Ya cancelaste!");
			}
		}else{
			response.setCode(400);
			response.setCodeMessage("El prestatario no coincide");
		}
		
		return response;
	}

	/**
	 * @Author Alejandro Bermudez Vargas
	 * @param ServiceContactRequest serviceContactRequest
	 * @return Retonrna el resultado de dicha respuesta
	 * @version 1.0
	 */
	@RequestMapping(value = "/answerContract", method = RequestMethod.POST)
	public ServiceContactResponse answerContract(@RequestBody ServiceContactRequest serviceContactRequest) {
		String streventId = AES.base64decode(serviceContactRequest.getEventId());
		String strserviceId = AES.base64decode(serviceContactRequest.getServiceId());
		ServiceContactResponse response = new ServiceContactResponse();
		ServiceContact serviceContact = serviceContactInterface
				.getByServiceServiceIdAndEventEventId(Integer.parseInt(strserviceId), Integer.parseInt(streventId));
		if (serviceContact.getState() == 0) {
			serviceContact.setState(serviceContactRequest.getState());
			response.setCode(200);
			if (serviceContactRequest.getState() == 2)
				response.setCodeMessage("Solicitud aceptada");
			if (serviceContactRequest.getState() == 1)
				response.setCodeMessage("Solicitud no aceptada");
		}
		serviceContactInterface.saveServiceContact(serviceContact);
		return response;
	}
	
	/**
	 * @param eventParticipant Id encriptado del participante solicitado
	 * @param state nuevo estado del participante
	 * @return El participante y su estado
	 * @version 1.1
	 */
	@RequestMapping(value = "/getPaticipant/{eventParticipant}/{state}", method = RequestMethod.POST)
	public ServiceContactResponse getPaticipant(@PathVariable String eventParticipant, @PathVariable byte state) {
		int idParticipant = Integer.parseInt(AES.base64decode(eventParticipant));
		ServiceContactResponse response = new ServiceContactResponse();
		EventParticipant objEventParticipant = eventParticipantServiceInterface.findById(idParticipant);
		
		if(objEventParticipant.getState() != 3 && objEventParticipant.getState() != 4) {
			if (objEventParticipant.getState() == 1) {
				objEventParticipant.setState(state);
				response.setCode(200);
				response.setCodeMessage("Te han invitado a un evento");
			}else if(objEventParticipant.getState() == 2){
				response.setCode(201);
				response.setCodeMessage("Ya confirmaste!");
			}else if(objEventParticipant.getState() == 0){
				response.setCode(202);
				response.setCodeMessage("Ya cancelaste!");
			}
		}else{
			response.setCode(203);
			response.setCodeMessage("Fuiste bloqueado por el promotor");
		}
		
		return response;
	}

	/**
	 * @author Antoni Ramirez Montano (autor)
	 * @author Ernesto Mendez A. (Modificado)
	 * @param to parametro con el que se recibe la lista de correos
	 * @param eventId se recibe el id del evento para el cual han sido invitados
	 * @version 1.0
	 */
	@RequestMapping(value = "/sendEmailInvitation", method = RequestMethod.POST)
	public EventParticipantResponse sendEmailInvitation(@RequestBody ListSimplePOJO to, @QueryParam("eventId") int eventId) {
		EventParticipantResponse resp = new EventParticipantResponse();
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		String subject = "Invitación a un evento";
		
		try{
			String email = to.getListSimple().get(0);
			
			EventParticipant eventParticipant = new EventParticipant();
			eventParticipant.setEvent(new Event());
			eventParticipant.getEvent().setEventId(eventId);
			eventParticipant.setState((byte) 1);
			
			User user = userserviceInterface.findByEmail(email);
			
			if(user != null){
				eventParticipant.setUser(user);
			}else{
				OfflineUser offlineUser = new OfflineUser();
				offlineUser.setEmail(email);
				eventParticipant.setOfflineUser(offlineUser);
			}
			
			eventParticipant.setInvitationDate(new Date());
			Boolean stateResponse = eventParticipantServiceInterface.saveParticipant(eventParticipant);
			
			String text = "http://localhost:8080/dondeEs/#/landingPage/?eventId="
					+ AES.base64encode(String.valueOf(eventId)) + "&email=" + AES.base64encode(email)
					+ "&eventParticipantId="
					+ AES.base64encode(String.valueOf(eventParticipant.getEventParticipantId()));

			if(stateResponse){
				resp.setCode(200);
				resp.setCodeMessage("Successful");
			}else{
				resp.setCode(500);
				resp.setCodeMessage("Something is wrong");
			}
			
			mailMessage.setTo(email);
			mailMessage.setText(text);
			mailMessage.setSubject(subject);
			
			new Thread("sendParticipantInvitation"){
				public void run(){
					mailSender.send(mailMessage);
				}
			}.start();
		}catch(Exception ae){
			ae.printStackTrace();
			resp.setCode(500);
			resp.setCodeMessage("Exception was thrown");
		}
		
		return resp;
	}
	
	@RequestMapping(value = "/insertUserImage", method = RequestMethod.POST)
	public UserResponse insertUserImage(@RequestParam("email") String email, @RequestParam("file") MultipartFile file) {
		UserResponse us = new UserResponse();
		User u = userServiceInterface.findByEmail(email);
		String image = Utils.writeToFile(file, servletContext);
		if (image != null)
			u.setImage(image);
		Boolean nuserId = userServiceInterface.updateUser(u);
		if (nuserId) {
			us.setCode(200);
			us.setCodeMessage("User update succesfully");
		} else {
			us.setCode(400);
			us.setCodeMessage("El usuario ya existe en la base de datos!");
		}

		return us;
	}
}
