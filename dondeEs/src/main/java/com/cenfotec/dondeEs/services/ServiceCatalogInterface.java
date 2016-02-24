package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.ServiceCatalog;

public interface ServiceCatalogInterface {

	List<ServiceCatalog> getAll();
	
	ServiceCatalog getById(int pidServiceCatalog);
}
