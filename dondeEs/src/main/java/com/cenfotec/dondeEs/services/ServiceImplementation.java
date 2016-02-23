package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.*;

import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.repositories.ServiceRepository;

@Service
public class ServiceImplementation implements ServiceInterface {
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Override
	public Boolean saveService(com.cenfotec.dondeEs.ejb.Service service) {
		com.cenfotec.dondeEs.ejb.Service nservice =  serviceRepository.save(service);
	 	return (nservice == null) ? false : true;
	}
	
	@Override
	public List<com.cenfotec.dondeEs.ejb.Service> getAll(){
		List<com.cenfotec.dondeEs.ejb.Service> listService = serviceRepository.findAll();
		return listService;
	}
	
}
