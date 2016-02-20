package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.EventRepository;

@Service
public class EventService implements EventServiceInterface{
	@Autowired private EventRepository eventRepository;
}
