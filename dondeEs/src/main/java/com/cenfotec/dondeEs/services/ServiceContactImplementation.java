package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.repositories.ServiceContactRepository;

@Service
public class ServiceContactImplementation implements ServiceContactInterface {
	
	@Autowired private ServiceContactRepository contactRepository;
	
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
			BeanUtils.copyProperties(ta, serviceContactPOJO);
			if(ta.getEvent()!=null){
				EventPOJO eventPojo = new EventPOJO();
				BeanUtils.copyProperties(ta.getEvent(),eventPojo );
				eventPojo.setPlace(null);
				eventPojo.setUser(null);
				eventPojo.setServiceContacts(null);
				eventPojo.setChats(null);
				eventPojo.setEventParticipants(null);
				eventPojo.setNotes(null);
				serviceContactPOJO.setEvent(eventPojo);
			}
			if(ta.getService()!=null){
				ServicePOJO servicePojo = new ServicePOJO();
				BeanUtils.copyProperties(ta.getService(),servicePojo );
				servicePojo.setUser(null);
				serviceContactPOJO.setService(servicePojo);
			}
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

}
