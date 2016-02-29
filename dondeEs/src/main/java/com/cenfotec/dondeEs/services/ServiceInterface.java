package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Service;



public interface ServiceInterface {
	
	Boolean saveService(Service service);
	
	List<Service> getAll();
	
	

}
