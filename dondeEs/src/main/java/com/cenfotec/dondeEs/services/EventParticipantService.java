package com.cenfotec.dondeEs.services;


import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.EventParticipant;

import com.cenfotec.dondeEs.repositories.EventParticipantRepository;

@Service
public class EventParticipantService implements EventParticipantServiceInterface {

	
	@Autowired private EventParticipantRepository eventParticipantRepository;
	
	@Override
	@Transactional
	public List<EventParticipantPOJO> getAllEventParticipants(int idEvent){
		
		List<EventParticipant> eventParticipantList = eventParticipantRepository.findEventParticipantByEventId(idEvent);
		List<EventParticipantPOJO> listPojo = new ArrayList<EventParticipantPOJO>();
		
		eventParticipantList.stream().forEach(ep -> {
			EventParticipantPOJO eventParticipantPOJO = new EventParticipantPOJO();
			BeanUtils.copyProperties(ep, eventParticipantPOJO);
			eventParticipantPOJO.setEvent(null);
			if(ep.getUser()!=null){
				UserPOJO userPojo = new UserPOJO();
				userPojo.setName(ep.getUser().getName());
				userPojo.setLastName1(ep.getUser().getLastName1());
				userPojo.setLastName2(ep.getUser().getLastName2());
				userPojo.setPhone(ep.getUser().getPhone());
				userPojo.setEmail(ep.getUser().getEmail());
				eventParticipantPOJO.setUser(userPojo);
			}
			listPojo.add(eventParticipantPOJO);
		});
		return listPojo;
	}

	@Override
	@Transactional
	public Boolean saveParticipant(EventParticipant peventParticipant) {
		EventParticipant eventParticipant = eventParticipantRepository.save(peventParticipant);
		return (eventParticipant == null) ? false : true;
	}
	
	@Override
	public EventParticipant findById(int id) {
		EventParticipant eventParticipant = eventParticipantRepository.findOne(id);
		return eventParticipant;
	}

}
