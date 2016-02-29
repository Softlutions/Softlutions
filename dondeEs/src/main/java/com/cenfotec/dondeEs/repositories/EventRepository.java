package com.cenfotec.dondeEs.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
	List<Event> findAllByState(byte state);
	List<Event> findAllByUserUserId(int user_id);
	Event findByEventId(int idEvent);
}