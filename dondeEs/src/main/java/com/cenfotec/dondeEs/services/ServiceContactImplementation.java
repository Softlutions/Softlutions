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
		List<ServiceContact> listServiceContact = contactRepository.findByEventId(idEvent);
		List<ServiceContactPOJO> listPojo = new ArrayList<ServiceContactPOJO>();
		listServiceContact.stream().forEach(ta -> {
			ServiceContactPOJO serviceContactPOJO = new ServiceContactPOJO();
			BeanUtils.copyProperties(ta, serviceContactPOJO);
			if(ta.getEvent()!=null){
				EventPOJO eventPojo = new EventPOJO();
				BeanUtils.copyProperties(ta.getEvent(),eventPojo );
				serviceContactPOJO.setEvent(eventPojo);
			}
			if(ta.getService()!=null){
				ServicePOJO servicePojo = new ServicePOJO();
				BeanUtils.copyProperties(ta.getService(),servicePojo );
				serviceContactPOJO.setService(servicePojo);
			}
			listPojo.add(serviceContactPOJO);
		});
		return listPojo;
	}

}
