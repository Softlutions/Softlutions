package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.ServiceCatalog;
import com.cenfotec.dondeEs.pojo.ServiceCatalogPOJO;;

public interface ServiceCatalogRepository extends CrudRepository<ServiceCatalog, Integer> {

	List<ServiceCatalog> findAll(); 
	
	
	
}
