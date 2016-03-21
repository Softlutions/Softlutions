package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.Place;
import com.cenfotec.dondeEs.ejb.User;
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
			eventPOJO.setEventParticipants(null);
			eventPOJO.setServiceContacts(null);
			eventPOJO.setChats(null);
			eventPOJO.setNotes(null);
			eventPOJO.setServiceContacts(null);
			eventPOJO.setPlace(placePOJO);
			eventPOJO.setChats(null);
			eventsPOJO.add(eventPOJO);
		});
		return eventsPOJO;
	}
	
	/***
	 * Obtiene todos los eventos publicados.
	 * @author Enmanuel García González
	 * @version 1.0
	 */
	@Override
	public List<EventPOJO> getAllEventPublish() {			
		List<EventPOJO> eventsPOJO = new ArrayList<>();
		eventRepository.findAllByState((byte) 3).stream().forEach(e -> {
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
	public int saveEvent(Event _event) {
		Event event = eventRepository.save(_event);
		return event.getEventId();
	}

	@Override
	public EventPOJO eventById(int idEvent) {

		//Event
		Event event = eventRepository.findOne(idEvent);
		EventPOJO eventPOJO = new EventPOJO();
		eventPOJO.setEventId(event.getEventId());
		eventPOJO.setDescription(event.getDescription());
		eventPOJO.setImage(event.getImage());
		eventPOJO.setLargeDescription(event.getLargeDescription());
		eventPOJO.setName(event.getName());
		eventPOJO.setPrivate_(event.getPrivate_());
		eventPOJO.setPublishDate(event.getPublishDate());
		eventPOJO.setRegisterDate(event.getRegisterDate());
		eventPOJO.setState(event.getState());
		
		if(event.getPlace() != null){
			Place place = event.getPlace();
			PlacePOJO placePOJO = new PlacePOJO();
			placePOJO.setPlaceId(place.getPlaceId());
			placePOJO.setLatitude(place.getLatitude());
			placePOJO.setLongitude(place.getLongitude());
			placePOJO.setName(place.getName());
			eventPOJO.setPlace(placePOJO);
		}
		if(event.getUser() != null){
			User user  = event.getUser();
			UserPOJO userPOJO = new UserPOJO();
			userPOJO.setUserId(user.getUserId());
			userPOJO.setEmail(user.getEmail());
			userPOJO.setImage(user.getImage());
			userPOJO.setLastName1(user.getLastName1());
			userPOJO.setLastName2(user.getLastName2());
			userPOJO.setName(user.getName());
			userPOJO.setPassword(user.getPassword());
			userPOJO.setPhone(user.getPhone());
			if(user.getState()==1) userPOJO.setState(true);
			if(user.getState()==0) userPOJO.setState(false);
			eventPOJO.setUser(userPOJO);
		}
		return eventPOJO;
	}
}
