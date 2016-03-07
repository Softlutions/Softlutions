package com.cenfotec.dondeEs.controller;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.ServiceContactRequest;
import com.cenfotec.dondeEs.contracts.ServiceContactResponse;
import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.logic.AES;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;
import com.cenfotec.dondeEs.services.ServiceContactInterface;

@RestController
@RequestMapping(value = "rest/protected/serviceContact")
public class ServiceContactController {

	@Autowired
	private ServiceContactInterface serviceContactInterface;

	@RequestMapping(value = "/getAllServiceContact/{idEvent}", method = RequestMethod.GET)
	public ServiceContactResponse getAllServiceContact(@PathVariable("idEvent") int idEvent) {
		ServiceContactResponse response = new ServiceContactResponse();
		response.setListContracts(serviceContactInterface.getAllServiceContacts(idEvent));
		return response;
	}

	@RequestMapping(value = "/createServiceContact", method = RequestMethod.POST)
	public ServiceContactResponse createServiceContact(@RequestBody ServiceContact serviceContact) {
		ServiceContactResponse response = new ServiceContactResponse();
		Boolean state = serviceContactInterface.saveServiceContact(serviceContact);
		if (state) {
			response.setCode(200);
			response.setCodeMessage("Succesfull");
		} else {
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		return response;
	}

	@RequestMapping(value = "/answerContract", method = RequestMethod.POST)
	public ServiceContactResponse answerContract(@RequestBody ServiceContactRequest serviceContactRequest) {
		String streventId = AES.base64decode(serviceContactRequest.getEventId());
		String strserviceId = AES.base64decode(serviceContactRequest.getServiceId());
		ServiceContactResponse response = new ServiceContactResponse();
		ServiceContact serviceContact = serviceContactInterface
				.getByServiceServiceIdAndEventEventId(Integer.parseInt(streventId), Integer.parseInt(strserviceId));
		if (serviceContact.getState() == 0) {
			serviceContact.setState(serviceContactRequest.getState());
			response.setCode(200);
			response.setCodeMessage("Asistiras!");
		} else {
			response.setCode(500);
			response.setCodeMessage("Ya confirmaste");
		}
		serviceContactInterface.saveServiceContact(serviceContact);
		return response;
	}
}