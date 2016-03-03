package com.cenfotec.dondeEs.controller;

import java.text.ParseException;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.EventParticipantResponse;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.ejb.User;
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

	@RequestMapping(value = "/createEventParticipant/{id}", method = RequestMethod.POST)
	public EventParticipantResponse createEventParticipant(@PathVariable("id") int id, @QueryParam("state") byte state, @QueryParam("user") Integer userId)
			throws ParseException {

		EventParticipantResponse response = new EventParticipantResponse();

		EventParticipant eventParticipant = new EventParticipant();
		eventParticipant.setEvent(new Event());
		eventParticipant.getEvent().setEventId(id);
		eventParticipant.setState(state);
		if (userId != null) {
			User user = new User();
			user.setUserId(userId);
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
