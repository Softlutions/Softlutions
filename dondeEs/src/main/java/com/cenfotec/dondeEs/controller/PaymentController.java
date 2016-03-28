package com.cenfotec.dondeEs.controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.EventRequest;
import com.cenfotec.dondeEs.contracts.EventResponse;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.services.EventServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/auction")
public class PaymentController {

	@Autowired
	EventServiceInterface eventServiceInterface;

	/***
	 * Notifica el pago de un evento.
	 * 
	 * @author Alejandro Bermudez Vargas
	 * @param eventPojo
	 * @return
	 * @version 2.0
	 */
	@RequestMapping(value = "/sendNotificationPayEvent", method = RequestMethod.PUT)
	public EventResponse sendNotificationPayEvent(@RequestBody EventRequest eventRequest) {
		EventResponse response = new EventResponse();
		try {
			if (eventRequest.getEvent().getEventId() != 0) {
				Event event = eventServiceInterface.getEventById(eventRequest.getEvent().getEventId());
				event.getEventParticipants();
				SendEmailController sendEmailController = new SendEmailController();
				ArrayList<UserPOJO> usersPOJO = new ArrayList<>();
				event.getEventParticipants().stream().forEach(ep -> {
					UserPOJO userPOJO = new UserPOJO();
					userPOJO.setEmail(ep.getUser().getEmail());
					usersPOJO.add(userPOJO);
				});
				EventPOJO eventPOJO = new EventPOJO();
				eventPOJO.setName(event.getName());
				sendEmailController.sendNotificationPayEvent(usersPOJO, eventPOJO);

			} else {
				response.setCode(409);
				response.setErrorMessage("idEvent is zero");
			}
		} catch (Exception e) {
			response.setCode(500);
			response.setCodeMessage(e.toString());
			e.printStackTrace();

		}
		return response;
	}

}