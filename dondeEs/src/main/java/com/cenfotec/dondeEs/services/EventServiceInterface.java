package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.contracts.EventRequest;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.pojo.EventPOJO;

public interface EventServiceInterface {
	List<EventPOJO> getAllEventByUser(int pidUsuario);
	Event getEventById(int idEvent);
	Boolean saveEvent(Event e);
	EventPOJO eventById(int idEvent);
	List<EventPOJO> getAllEventPublish();
}