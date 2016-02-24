package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventPublish;

public interface EventServiceInterface {
	List<Event> getAll();
	List<EventPublish> getAllEventPublish();
}
