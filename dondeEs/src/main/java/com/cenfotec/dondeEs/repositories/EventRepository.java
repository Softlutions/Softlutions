package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventPublish;

public interface EventRepository extends CrudRepository<Event, Integer>{
	List<Event> findAll();
	
	@Query(value = "select e.event_id, e.name eventName, e.description, e.large_description, e.image eventImage, " +
	"e.private, e.publish_date, " + 
	"p.name placeName, p.latitude, p.longitude, u.name userName, u.last_name1, u.last_name2, u.image userImage " + 
	"from event e inner join place p on e.place_id = p.place_id " +
	"inner join user u on e.user_id = u.user_id;", nativeQuery = true)
	List<EventPublish> findAllEventPublish();  
	
	@Query(value = "UPDATE event SET state = ?1, publish_date = ?2 WHERE event_id = ?3;", nativeQuery = true)
	boolean publishEvent(int state, String publishDate, int idEvent);
}
