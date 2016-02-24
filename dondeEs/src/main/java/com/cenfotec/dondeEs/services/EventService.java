package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventPublish;
import com.cenfotec.dondeEs.repositories.EventRepository;

@Service
public class EventService implements EventServiceInterface{
	@Autowired private EventRepository eventRepository;

	@Override
	public List<Event> getAll() {
		List<Event> listEvent = eventRepository.findAll();
		return listEvent;
	}

	@Override
	public List<EventPublish> getAllEventPublish() {
		List<EventPublish> listEventPublish = eventRepository.findAllEventPublish();
		return listEventPublish;
	}
}