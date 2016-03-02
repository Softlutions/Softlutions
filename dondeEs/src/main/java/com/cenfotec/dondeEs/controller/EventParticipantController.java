package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.cenfotec.dondeEs.contracts.EventParticipantResponse;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/eventParticipant")
public class EventParticipantController {
	
	@Autowired private EventParticipantServiceInterface eventParticipantServiceInterface;
	
	@RequestMapping(value ="/getAllEventParticipants/{idEvent}", method = RequestMethod.GET)
	public EventParticipantResponse getAllEventParticipants(@PathVariable("idEvent") int idEvent){
		EventParticipantResponse response = new EventParticipantResponse();
		response.setEventParticipantsList(eventParticipantServiceInterface.getAllEventParticipants(idEvent));
		return response;
	}

}
