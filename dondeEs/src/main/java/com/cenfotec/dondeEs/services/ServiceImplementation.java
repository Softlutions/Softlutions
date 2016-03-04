package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.cenfotec.dondeEs.ejb.Service;
import org.springframework.transaction.annotation.Transactional;


import com.cenfotec.*;

import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
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
	
	@Override
	@Transactional
	public ServicePOJO getService(int idEvent){
		com.cenfotec.dondeEs.ejb.Service nservice = serviceRepository.findOne(idEvent);
		ServicePOJO servicePOJO = new ServicePOJO();
		BeanUtils.copyProperties(nservice, servicePOJO);
		return servicePOJO;
	}
	
	@Transactional
	public ServicePOJO getServiceById(int idService){
		com.cenfotec.dondeEs.ejb.Service nservice = serviceRepository.findOne(idService);
		ServicePOJO servicePOJO = new ServicePOJO();
		BeanUtils.copyProperties(nservice, servicePOJO);
		UserPOJO userPOJO = new UserPOJO();
		BeanUtils.copyProperties(nservice.getUser(), userPOJO);
		servicePOJO.setUser(userPOJO);
		return servicePOJO;
	}

	
}
