package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.contracts.ContractNotification;
import com.cenfotec.dondeEs.controller.SendEmailController;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.repositories.EventRepository;
import com.cenfotec.dondeEs.repositories.ServiceContactRepository;
import com.cenfotec.dondeEs.repositories.ServiceRepository;

@Service
public class ServiceContactImplementation implements ServiceContactInterface {
	
	@Autowired private ServiceContactRepository contactRepository;
	@Autowired private ServiceRepository serviceRepository;
	@Autowired private EventRepository eventRepository;
	@Autowired private SendEmailController sendEmailController;
	
	@Override
	public List<ServiceContact> getAll() {
		List<ServiceContact> contracts = contactRepository.findAll();
		return contracts;
	}
	
	@Override
	@Transactional
	public List<ServiceContactPOJO> getAllServiceContacts(int idEvent){
		List<ServiceContact> listServiceContact = contactRepository.findServiceContactByEventId(idEvent);
		List<ServiceContactPOJO> listPojo = new ArrayList<ServiceContactPOJO>();
		listServiceContact.stream().forEach(ta -> {
			ServiceContactPOJO serviceContactPOJO = new ServiceContactPOJO();
			serviceContactPOJO.setComment(ta.getComment());
			serviceContactPOJO.setState(ta.getState());
			serviceContactPOJO.setServiceContractId(ta.getServiceContractId());
			ServicePOJO servicePojo = new ServicePOJO();
			servicePojo.setName(ta.getService().getName());
			serviceContactPOJO.setService(servicePojo);
			listPojo.add(serviceContactPOJO);
		});
		return listPojo;
	}
	
	@Override
	public Boolean saveServiceContact(ServiceContact service) {
		ServiceContact serviceContact =  contactRepository.save(service);
	 	return (serviceContact == null) ? false : true;
	}
	
	@Override
	public ServiceContact getByServiceServiceIdAndEventEventId(int eventId, int serviceId) {
		return contactRepository.getByServiceServiceIdAndEventEventId(eventId, serviceId);
	}

	@Override
	@Transactional
	public Boolean cancelServiceContact(int contractID, ServiceContact service) {
		ServiceContact serviceContact = null;
		
		if(contractID == service.getServiceContractId()){
			serviceContact = contactRepository.findOne(service.getServiceContractId());
			
			if(serviceContact != null){
				serviceContact.setState((byte) 2);
				contactRepository.save(serviceContact);
			}
		}
		
	 	return (serviceContact == null) ? false : true;
	}
	
	@Override
	@Transactional
	public Boolean contractService(int pservice, int pevent){		
		com.cenfotec.dondeEs.ejb.Service service = serviceRepository.findOne(pservice);
		Event event = eventRepository.findOne(pevent);
		ContractNotification contractNotification = new ContractNotification();
		ServiceContact serviceContact = contactRepository.findByServiceServiceIdAndEventEventId(pservice, pevent);
		
		
		
		EventPOJO eventPOJO = new EventPOJO();
		eventPOJO.setEventId(event.getEventId());
		contractNotification.setEvent(eventPOJO);
		
		ServicePOJO servicePOJO = new ServicePOJO();
		servicePOJO.setServiceId(service.getServiceId());
		contractNotification.setService(servicePOJO);
		
		UserPOJO userPOJO = new UserPOJO();
		userPOJO.setUserId(service.getUser().getUserId());
		servicePOJO.setUser(userPOJO);
		Boolean isValid;
		if(serviceContact == null){
			isValid = true;
			serviceContact = new ServiceContact();
			serviceContact.setEvent(event);
			serviceContact.setService(service);
			contactRepository.save(serviceContact);
			
			sendEmailController.sendEmailContractNotification(contractNotification);
		}else{
			isValid = false;
		}
		return isValid;
	}

}
