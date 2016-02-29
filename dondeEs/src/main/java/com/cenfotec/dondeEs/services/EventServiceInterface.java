package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Event;
<<<<<<< HEAD
import com.cenfotec.dondeEs.pojo.EventPOJO;

public interface EventServiceInterface {
	List<EventPOJO> getAllEventByUser(int pidUsuario);
	Event getEventById(int idEvent);
	Boolean saveEvent(Event e);
	List<Event> getAllEventPublish();
}