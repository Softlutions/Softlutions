package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;

public interface ServiceContactInterface {
	
	public List<ServiceContact> getAll();

	List<ServiceContactPOJO> getAllServiceContacts(int idUser);
	
	Boolean saveServiceContact(ServiceContact service);

	
}
