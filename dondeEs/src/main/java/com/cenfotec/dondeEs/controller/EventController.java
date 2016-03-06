package com.cenfotec.dondeEs.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.EventResponse;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.services.EventServiceInterface;
import com.cenfotec.dondeEs.services.UserService;

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
	 * @author Enmanuel García González
	 * @return
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
	 * @author Enmanuel García González
	 * @param eventRequest
	 * @return
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
	 * @author Enmanuel García González	
	 * @param eventRequest
	 * @return
	 */
	@Transactional
	@RequestMapping(value ="/cancelEvent", method = RequestMethod.GET)
	public EventResponse cancelEvent() { // @RequestBody EventPOJO eventRequest
		EventResponse response = new EventResponse();	
		boolean state;
		
		UserService us = new UserService();
		
		List<UserPOJO> servicesProviders = us.getAllServicesProviderAuction(1);
		
	/*	if(eventRequest.getEventId() != 0){		
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
		} */
		return response;
	} 
}