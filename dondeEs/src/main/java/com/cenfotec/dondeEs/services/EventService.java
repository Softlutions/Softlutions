package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.repositories.EventRepository;

@Service
public class EventService implements EventServiceInterface{
	@Autowired private EventRepository eventRepository;

	@Override
	public List<Event> getAllEventPublish() {
		return eventRepository.findAllByState(1);
	}

/*	@Override
	public Event getEventById(int idEvent) {
		return eventRepository.publishEvent(idEvent); 
	} */
}