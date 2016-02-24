package com.cenfotec.dondeEs.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.ServiceContactResponse;
import com.cenfotec.dondeEs.contracts.ServiceResponse;
import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.services.ServiceContactInterface;
import com.cenfotec.dondeEs.services.ServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/serviceContact")
public class ServiceContactController {

	@Autowired private ServiceContactInterface serviceContactInterface;
	
	@RequestMapping(value ="/getAllServiceContact", method = RequestMethod.GET)
	public ServiceContactResponse getAllServiceContact(){
		ServiceContactResponse response = new ServiceContactResponse();
		response.setListContracts(serviceContactInterface.getAll());
		return response;
	}
	
	
}