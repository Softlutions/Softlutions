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
		List<Event> events = eventRepository.findAllByState((byte) 1);
		events.forEach(event -> event.getUser().setRole(null));
		return events;
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