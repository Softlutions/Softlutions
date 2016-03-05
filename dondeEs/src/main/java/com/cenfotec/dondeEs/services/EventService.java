package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.PlacePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.repositories.EventRepository;

@Service
public class EventService implements EventServiceInterface {
	@Autowired
	private EventRepository eventRepository;

	@Override
	public List<EventPOJO> getAllEventByUser(int pidUsuario) {
		List<EventPOJO> eventsPOJO = new ArrayList<>();
		eventRepository.findAllByUserUserId(pidUsuario).stream().forEach(e -> {
			EventPOJO eventPOJO = new EventPOJO();
			PlacePOJO placePOJO = new PlacePOJO();
			BeanUtils.copyProperties(e, eventPOJO);
			BeanUtils.copyProperties(e.getPlace(), placePOJO);
			eventPOJO.setPlace(placePOJO);
			eventsPOJO.add(eventPOJO);
		});
		return eventsPOJO;
	}
	
	@Override
	public List<EventPOJO> getAllEventPublish() {			
		List<EventPOJO> eventsPOJO = new ArrayList<>();
		eventRepository.findAllByState((byte) 1).stream().forEach(e -> {
			EventPOJO eventPOJO = new EventPOJO();
			PlacePOJO placePOJO = new PlacePOJO();
			UserPOJO userPOJO = new UserPOJO();
			BeanUtils.copyProperties(e, eventPOJO);
			eventPOJO.setChats(null);
			eventPOJO.setEventParticipants(null);
			eventPOJO.setNotes(null);
			eventPOJO.setServiceContacts(null);
			BeanUtils.copyProperties(e.getPlace(), placePOJO);
			BeanUtils.copyProperties(e.getUser(), userPOJO);
			userPOJO.setEventParticipants(null);
			userPOJO.setChats(null);
			userPOJO.setMessages(null);
			userPOJO.setPasswordHistories(null);
			userPOJO.setTermConditions(null);
			userPOJO.setUsers1(null);
			userPOJO.setUsers2(null);
			userPOJO.setUserType(null);
			eventPOJO.setPlace(placePOJO);
			eventPOJO.setUser(userPOJO);
			eventsPOJO.add(eventPOJO);
		});
		
		return eventsPOJO;
	}

	@Override
	public Event getEventById(int idEvent) {
		return eventRepository.findByEventId(idEvent);
	}

	@Override
	public Boolean saveEvent(Event _event) {
		Event event = eventRepository.save(_event);
		return (event == null) ? false : true;
	}
}
