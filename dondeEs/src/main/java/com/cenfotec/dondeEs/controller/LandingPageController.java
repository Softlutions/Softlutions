package com.cenfotec.dondeEs.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.contracts.CommentResponse;
import com.cenfotec.dondeEs.contracts.EventImageResponse;
import com.cenfotec.dondeEs.contracts.EventParticipantResponse;
import com.cenfotec.dondeEs.contracts.EventResponse;
import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.services.CommentServiceInterface;
import com.cenfotec.dondeEs.services.EventImageServiceInterface;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;
import com.cenfotec.dondeEs.services.EventServiceInterface;

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
	@RequestMapping(value = "/getImagesByEventId/{id}", method = RequestMethod.GET)
	public EventImageResponse getAllByUser(@PathVariable("id") int id) {
		EventImageResponse response = new EventImageResponse();
		response.setCode(200);
		response.setCodeMessage("Success");
		response.setImages(eventImageServiceInterface.getAllByEventId(id));
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
	
}
