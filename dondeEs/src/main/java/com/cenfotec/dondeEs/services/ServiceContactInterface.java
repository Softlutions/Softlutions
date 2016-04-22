package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;

public interface ServiceContactInterface {
	public List<ServiceContact> getAll();

	List<ServiceContactPOJO> getContractsLeftByPromoter(int promoterId);
	List<ServiceContactPOJO> getAllServiceContacts(int idEvent);

	Boolean saveServiceContact(ServiceContact service);
	ServiceContact getByServiceServiceIdAndEventEventId(int serviceId, int eventId);
	Boolean cancelServiceContact(int contractID, ServiceContact service);
	Boolean contractService(int pservice, int pevent);
	
	Boolean responseContract(int serviceContractId, byte state);
}
