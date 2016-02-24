package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.ServiceCatalog;
import com.cenfotec.dondeEs.repositories.ServiceCatalogRepository;

@Service
public class ServiceCatalogImplementation implements ServiceCatalogInterface {

	@Autowired private ServiceCatalogRepository catalogRepository;
	
	@Override
	public List<ServiceCatalog> getAll(){
		List<ServiceCatalog> listService = catalogRepository.findAll();
		return listService;
	}
	
	@Override
	public ServiceCatalog getById(int pidServiceCatalog){
		return catalogRepository.findOne(pidServiceCatalog);
	}
}
