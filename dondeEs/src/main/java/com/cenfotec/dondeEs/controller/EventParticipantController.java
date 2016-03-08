package com.cenfotec.dondeEs.controller;


import java.text.ParseException;
import java.util.Date;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.cenfotec.dondeEs.contracts.EventParticipantResponse;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;
import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.services.CommentServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/eventParticipant")
public class EventParticipantController {
	
	@Autowired private CommentServiceInterface commentServiceInterface;
	@Autowired private EventParticipantServiceInterface eventParticipantServiceInterface;
	
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
	
	//revisar si alguien hizo esto porq yo no fui atte:JK y hace lo mismo que el mio
	
	@RequestMapping(value = "/getAllEventParticipantByEvent/{id}", method = RequestMethod.GET)
	public EventParticipantResponse getAllEventParticipantByEvent(@PathVariable("id") int id) {
		EventParticipantResponse response = new EventParticipantResponse();
		return response;
	}

	@RequestMapping(value = "/createEventParticipant/{id}", method = RequestMethod.PUT)
	public EventParticipantResponse createEventParticipant(@PathVariable("id") int id, @QueryParam("state") byte state, @QueryParam("comment") String comment)
			throws ParseException {

		EventParticipantResponse response = new EventParticipantResponse();

		EventParticipant eventParticipant = eventParticipantServiceInterface.findById(id);
		
		eventParticipant.setState(state);
		
		Comment ncomment = new Comment();
		ncomment.setContent(comment);
		ncomment.setDate(new Date());
		ncomment.setEventParticipant(eventParticipant);
		
		Boolean stateComment = commentServiceInterface.saveComment(ncomment);
		if(stateComment){
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
}