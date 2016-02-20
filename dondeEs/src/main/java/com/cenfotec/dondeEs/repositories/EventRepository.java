package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Event;

public interface EventRepository extends CrudRepository<Event, Integer>{

	List<Event> findAll();
}
