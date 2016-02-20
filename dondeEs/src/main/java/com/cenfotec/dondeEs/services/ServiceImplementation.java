package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.ServiceRepository;

@Service
public class ServiceImplementation implements ServiceInterface {
	@Autowired
	private ServiceRepository serviceRepository;
}
