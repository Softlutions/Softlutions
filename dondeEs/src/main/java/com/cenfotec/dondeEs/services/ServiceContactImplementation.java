package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.repositories.ServiceContactRepository;

@Service
public class ServiceContactImplementation implements ServiceContactInterface {
@Autowired private ServiceContactRepository contactRepository;

@Override
public List<ServiceContact> getAll() {
	List<ServiceContact> contracts = contactRepository.findAll();
	return contracts;
}
}
