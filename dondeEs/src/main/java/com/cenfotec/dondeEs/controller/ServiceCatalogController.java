package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.ServiceCatalogResponse;
import com.cenfotec.dondeEs.services.ServiceCatalogInterface;

@RestController
@RequestMapping(value = "rest/protected/serviceCatalog")
public class ServiceCatalogController {

	@Autowired private ServiceCatalogInterface serviceCatalogInterface;
	
	@RequestMapping(value ="/getAllCatalogService", method = RequestMethod.GET)
	public ServiceCatalogResponse getAllService(){
		ServiceCatalogResponse response = new ServiceCatalogResponse();
		response.setServiceCatalogList(serviceCatalogInterface.getAll());
		return response;
	}
	
	
}
