package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.ServiceCatalog;
import com.cenfotec.dondeEs.pojo.ServiceCatalogPOJO;
import com.cenfotec.dondeEs.repositories.ServiceCatalogRepository;

@Service
public class ServiceCatalogImplementation implements ServiceCatalogInterface {

	@Autowired private ServiceCatalogRepository catalogRepository;
	
	@Override
	public List<ServiceCatalogPOJO> getAll(){
		List<ServiceCatalog> listService =  catalogRepository.findAll();
		List<ServiceCatalogPOJO> listPojo = new ArrayList<ServiceCatalogPOJO>();
		listService.stream().forEach(ta-> {
			ServiceCatalogPOJO servicePOJO = new ServiceCatalogPOJO();
			BeanUtils.copyProperties(ta, servicePOJO);
			servicePOJO.setAuctions(null);
			
			listPojo.add(servicePOJO);
		});
		return listPojo;
	}
	

}
