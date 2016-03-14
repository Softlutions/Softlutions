package com.cenfotec.dondeEs.controller;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.EventResponse;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.logic.AES;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.services.EventServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/event")
public class EventController {
	
	@Autowired private EventServiceInterface eventServiceInterface;
	
	@RequestMapping(value ="/getAllEventByUser/{id}", method = RequestMethod.GET)
	public EventResponse getAllByUser(@PathVariable("id") int id){				
		EventResponse response = new EventResponse();
		response.setCode(200);
		response.setCodeMessage("Events by user");
		response.setEventList(eventServiceInterface.getAllEventByUser(id));
		return response;
	}
	
	/***
	 * Obtiene todos los eventos que han sido publicados.
	 * @author Enmanuel García González
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(value ="/getAllEventPublish", method = RequestMethod.GET)
	public EventResponse getAll(){				
		EventResponse response = new EventResponse();
		response.setCode(200);
		response.setCodeMessage("eventsPublish fetch success");
		response.setEventList(eventServiceInterface.getAllEventPublish());
		return response;
	}
	
	/***
	 * Publica un determinado evento.
	 * @author Enmanuel García González
	 * @param eventRequest
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(value ="/publishEvent", method = RequestMethod.PUT)
	public EventResponse publishEvent(@RequestBody EventPOJO eventRequest) {
		EventResponse response = new EventResponse();	
		
		if(eventRequest.getEventId() != 0){
			Event event =  eventServiceInterface.getEventById(eventRequest.getEventId());
			event.setState((byte) 1);
			event.setPublishDate(new Date());
	 		
			boolean state = eventServiceInterface.saveEvent(event);
			
			if (state) {
				response.setCode(200);
				response.setErrorMessage("success");
			} else {
				response.setCode(500);
				response.setErrorMessage("publish event error");
			}

		} else {
			response.setCode(409);
			response.setErrorMessage("idEvent is zero");
		}
		
		return response;
	} 
	// get event by id
	@RequestMapping(value="/getEventById/{id}", method= RequestMethod.GET)
	public EventResponse getEventById(@PathVariable("id") int id){
		EventResponse response = new EventResponse();
		if(id != 0){
			response.setEventPOJO(eventServiceInterface.eventById(id));
			response.getEventPOJO().setPrivate_((byte)1);
			response.getEventPOJO().setState((byte)1);
			response.getEventPOJO().setPublishDate(new Date());
			response.setCode(200);
		}else{
			response.setCode(400);
			response.setCodeMessage("Something is wrong");
		}
		return response;
	}
	
	/***
	 * Cancela un evento que ha sido previamente publicado.
	 * @author Enmanuel García González	
	 * @param eventRequest
	 * @return
	 * @version 1.0
	 */
	@Transactional
	@RequestMapping(value ="/cancelEvent", method = RequestMethod.PUT)
	public EventResponse cancelEvent(@RequestBody EventPOJO eventRequest) { 
		EventResponse response = new EventResponse();	
		boolean state;
		
		if(eventRequest.getEventId() != 0){		
			try{
				Event event =  eventServiceInterface.getEventById(eventRequest.getEventId());
				event.setState((byte) 0);
				event.setPublishDate(new Date());
		 						
				state = eventServiceInterface.saveEvent(event);
				
				if (state) {
					response.setCode(200);
					response.setErrorMessage("success");
				} else {
					response.setCode(500);
					response.setErrorMessage("cancel event error");
				}
			} catch (Exception e) {
				response.setCode(500);
				response.setErrorMessage("internal error. " + e);
			}	
		} else {
			response.setCode(409);
			response.setErrorMessage("eventId is zero");
		} 
		return response;
	} 

	// PRUEBA DEL PROBLEMA CON LA INTERFACE 
	/*
	@Transactional
	@RequestMapping(value ="/cancelEvent", method = RequestMethod.GET)
	public EventResponse cancelEvent() { 
		EventResponse response = new EventResponse();	
		boolean state;
		
		UserService us = new UserService();
		
		List<UserPOJO> servicesProviders = us.getAllServicesProviderAuction(1);
		
		return response;
	} */
	
	// get event by id
		@RequestMapping(value="/getEventByEncryptId/{idEvent}", method= RequestMethod.GET)
		public EventResponse getEventByEncryptId(@PathVariable("idEvent") String id){
			EventResponse response = new EventResponse();
			int eventId = Integer.parseInt(AES.base64decode(id));
			if(eventId != 0){
				response.setEventPOJO(eventServiceInterface.eventById(eventId));
				response.getEventPOJO().setPrivate_((byte)1);
				response.getEventPOJO().setState((byte)1);
				response.getEventPOJO().setPublishDate(new Date());
				response.setCode(200);
			}else{
				response.setCode(400);
				response.setCodeMessage("Something is wrong");
			}
			return response;
		}
		
		/*@RequestMapping(value="/getActualEventStep", method= RequestMethod.GET)
		public EventResponse getActualEventStep(@PathVariable("idEvent") String id){
			EventResponse response = new EventResponse();
			int eventId = Integer.parseInt(AES.base64decode(id));
			if(eventId != 0){
				EventPOJO eventPOJO = eventServiceInterface.eventById(eventId);
				if(eventPOJO.getServiceContacts()==null) eventPOJO.setState(2);
				if(eventPOJO.getChats()==null) eventPOJO.setState(3);
				response.setEventPOJO(eventPOJO);
			}else{
				response.setCode(400);
				response.setCodeMessage("Something is wrong");
			}
			return response;
		}*/
}