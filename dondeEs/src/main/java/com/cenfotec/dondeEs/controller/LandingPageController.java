package com.cenfotec.dondeEs.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.contracts.BaseResponse;
import com.cenfotec.dondeEs.contracts.CommentResponse;
import com.cenfotec.dondeEs.contracts.ContractNotification;
import com.cenfotec.dondeEs.contracts.EventImageResponse;
import com.cenfotec.dondeEs.contracts.EventParticipantResponse;
import com.cenfotec.dondeEs.contracts.EventResponse;
import com.cenfotec.dondeEs.contracts.ServiceContactRequest;
import com.cenfotec.dondeEs.contracts.ServiceContactResponse;
import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.logic.AES;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.services.CommentServiceInterface;
import com.cenfotec.dondeEs.services.EventImageServiceInterface;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;
import com.cenfotec.dondeEs.services.EventServiceInterface;
import com.cenfotec.dondeEs.services.ServiceContactInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;

/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value = "rest/landing")
public class LandingPageController {

	@Autowired private EventParticipantServiceInterface eventParticipantServiceInterface;
	@Autowired private EventImageServiceInterface eventImageServiceInterface;
	@Autowired private CommentServiceInterface commentServiceInterface;
	@Autowired private EventServiceInterface eventServiceInterface;
	@Autowired private UserServiceInterface userServiceInterface;
	@Autowired private ServiceContactInterface serviceContactInterface;
	/**
	 * @author Ernesto Mendez A.
	 * @param eventParticipantId id del participante del evento asociado
	 * @param file archivo de imagen a subir
	 * @return si la operacion fue efectiva o no
	 * @version 1.0
	 */
	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	public EventImageResponse saveImage(@RequestParam("eventParticipantId") int eventParticipantId, @RequestParam("file") MultipartFile file) {
		EventImageResponse response = new EventImageResponse();
		
		if(eventImageServiceInterface.saveImage(eventParticipantId, file)){
			response.setCode(200);
			response.setCodeMessage("Success");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		
		return response;
	}
	
	/**
	 * @author Ernesto Mendez A.
	 * @param id id del evento del cual se desea objetener las imagenes asociadas
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
	 * @param eventId Peticion que contiene la información del comentario por crear.
	 * @return Respuesta del servidor de la petición.
	 * @version 1.0
	 */
	@RequestMapping(value ="/getCommentsByEvent/{eventId}", method = RequestMethod.GET)
	public CommentResponse getCommentsByEvent(@PathVariable("eventId") int eventId){
		CommentResponse response = new CommentResponse();
		response.setCommentList(commentServiceInterface.getCommentsByEvent(eventId));

		return response;
	}
	
	/**
	 * @author Juan Carlos Sánchez G. (Autor)
	 * @author Ernesto Mendez A. (Subir imagen)
	 * @param participantId id del participante que comenta
	 * @param content texto del comentario
	 * @param file (opcional) archivo de si comenta con una imagen
	 * @return Respuesta del servidor de la petición.
	 * @version 1.0
	 */
	@RequestMapping(value ="/saveComment", method = RequestMethod.POST)
	public CommentResponse saveComment(@RequestParam("participantId") int participantId, @RequestParam("content") String content, @RequestParam(value="file", required=false) MultipartFile file){
		CommentResponse response = new CommentResponse();
		
		Comment comment = new Comment();
		comment.setDate(new Date());
		comment.setContent(content);
		
		EventParticipant participant = eventParticipantServiceInterface.findById(participantId);
		comment.setEventParticipant(participant);
		
		if(participant != null){
			if(commentServiceInterface.saveComment(comment, file)){
				response.setCode(200);
				response.setCodeMessage("Succesful");
			}else{
				response.setCode(500);
				response.setCodeMessage("Internal error");
			}
		}else{
			response.setCode(404);
			response.setCodeMessage("Participant not found");
		}
		
		return response;
	}
	
	/**
	 * @param eventId id del evento a consultar
	 * @return evento consultado
	 * @version 1.0
	 */
	@RequestMapping(value = "/getEventById/{eventId}", method = RequestMethod.GET)
	public EventResponse getEventById(@PathVariable("eventId") int eventId) {
		EventResponse response = new EventResponse();
		EventPOJO event = eventServiceInterface.eventById(eventId);
		
		if (event != null) {
			response.setEventPOJO(event);
			response.setCode(200);
		} else {
			response.setCode(404);
			response.setCodeMessage("Request event not found");
		}
		
		return response;
	}
	
	/**
	 * @autor Ernesto Mendez A.
	 * @param userId id del usuario logueado
	 * @param eventId evento en el que participa
	 * @return respuesta del servidor
	 * @version 1.0
	 */
	@RequestMapping(value = "/getEventParticipantByUserAndEvent", method = RequestMethod.GET)
	public EventParticipantResponse getEventParticipantByUserAndEvent(@RequestParam("userId") int userId, @RequestParam("eventId") int eventId) {
		EventParticipantResponse response = new EventParticipantResponse();
		EventParticipantPOJO participant = eventParticipantServiceInterface.findByUserAndEvent(userId, eventId);
		
		if(participant != null){
			response.setCode(200);
			response.setCodeMessage("success");
			response.setEventParticipant(participant);
		}else{
			response.setCode(404);
			response.setCodeMessage("Participant not found!");
		}
		
		return response;
	}
	
	/**
	 * @author Ernesto Mendez A.
	 * @param userId id del usuario en sesion
	 * @param eventId id del evento
	 * @return id del nuevo participante
	 */
	@RequestMapping(value = "/createParticipant", method = RequestMethod.GET)
	public EventParticipantResponse createParticipant(@RequestParam("userId") int userId, @RequestParam("eventId") int eventId) {
		EventParticipantResponse response = new EventParticipantResponse();
		EventParticipantPOJO participant = eventParticipantServiceInterface.findByUserAndEvent(userId, eventId);
		
		if(participant != null){
			response.setCode(202);
			response.setCodeMessage("You already accepted");
			response.setEventParticipant(participant);
		}else{
			EventParticipant eventParticipant = new EventParticipant();
			eventParticipant.setUser(userServiceInterface.findById(userId));
			eventParticipant.setEvent(eventServiceInterface.getEventById(eventId));
			
			int nparticipantId = eventParticipantServiceInterface.createParticipant(eventParticipant);
			
			if(nparticipantId == 0){
				response.setCode(404);
				response.setCodeMessage("User or event not found!");
			}else{
				participant = new EventParticipantPOJO();
				participant.setEventParticipantId(nparticipantId);
				
				response.setCode(200);
				response.setCodeMessage("Success");
				response.setEventParticipant(participant);
			}
		}
		
		return response;
	}
	
	/**
	 * @author Antoni Ramirez Montano
	 * @param nameUser criterio a consultar
	 * @param namePlace criterio a consultar
	 * @param name criterio a consultar
	 * @param publishDate criterio a consultar
	 * @return lista de eventos basados en los criterios
	 */
	@RequestMapping(value="/getEventByParams/{nameUser}/{name}/{namePlace}", method= RequestMethod.GET)
	public EventResponse getEventByParams(@PathVariable("nameUser") String nameUser, @PathVariable("name") String name, @PathVariable("namePlace")String namePlace){
		EventResponse e = new EventResponse();
		e.setEventList(eventServiceInterface.getAllByParam(nameUser, name, namePlace, (byte)3));
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
		
		response.setCode(200);
		response.setCodeMessage("top events fetch success");
		response.setEventList(eventServiceInterface.getTopEventsByParticipants(top));
		
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
	// get event by id
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
	 * @Author Alejandro Bermudez Vargas
	 * @param ServiceContactRequest serviceContactRequest
	 * @return Retonrna el resultado de dicha  respuesta
	 * @version 1.0
	 */
	@RequestMapping(value = "/getServiceContact", method = RequestMethod.POST)
	public ServiceContactResponse getServiceContact(@RequestBody ServiceContactRequest serviceContactRequest) {
		String streventId = AES.base64decode(serviceContactRequest.getEventId());
		String strserviceId = AES.base64decode(serviceContactRequest.getServiceId());
		ServiceContactResponse response = new ServiceContactResponse();
		ServiceContact serviceContact = serviceContactInterface
				.getByServiceServiceIdAndEventEventId(Integer.parseInt(strserviceId), Integer.parseInt(streventId));
		
		if (serviceContact.getState() == 0) {
			serviceContact.setState(serviceContactRequest.getState());
			response.setCode(200);
			response.setCodeMessage("Tienes una solicitud pendiente");
		} else if(serviceContact.getState() == 2){
			response.setCode(201);
			response.setCodeMessage("Ya confirmaste!");
		}
		else if(serviceContact.getState() == 1){
			response.setCode(202);
			response.setCodeMessage("Ya cancelaste!");
		}
		return response;
	}
	
	
	/**
	 * @Author Alejandro Bermudez Vargas
	 * @param ServiceContactRequest serviceContactRequest
	 * @return Retonrna el resultado de dicha  respuesta
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
			if(serviceContactRequest.getState() == 2) response.setCodeMessage("Solicitud no aceptada");
			if(serviceContactRequest.getState() == 1) response.setCodeMessage("Solicitud no aceptada");
		}
		serviceContactInterface.saveServiceContact(serviceContact);
		return response;
	}
	
}
