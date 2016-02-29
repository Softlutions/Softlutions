package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cenfotec.dondeEs.ejb.Service;

import com.cenfotec.dondeEs.repositories.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceImplementation implements ServiceInterface {
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Override
	public Boolean saveService(Service pservice) {
		Service nservice =  serviceRepository.save(pservice);
	 	return (nservice == null) ? false : true;
	}
	
	@Override
	public List<com.cenfotec.dondeEs.ejb.Service> getAll(){
		List<com.cenfotec.dondeEs.ejb.Service> listService = serviceRepository.findAll();
		return listService;
	}
	
	
	
}
