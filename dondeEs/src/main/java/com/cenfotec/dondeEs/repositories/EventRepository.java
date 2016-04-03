package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
	List<Event> findAllByState(byte state);
	List<Event> findAllByUserUserId(int user_id);
	Event findByEventId(int idEvent);
	
	@Query("SELECT e FROM Event as e WHERE eventId = ?1")
	Event findByid(int idEvent);
	
//	@Query(value = "SELECT name, place_id, user_id, publish_date FROM event WHERE user_id = ?1 OR place_id = ?1 OR name= ?"+ 
//	"OR publish_date = ?1", nativeQuery = true)
	@Query("Select e from Event as e WHERE e.state = ?1 and (e.user.name = ?2 OR e.name = ?3 OR e.place.name= ?4)")
	List<Event> finByParams(byte state, String nameUser, String name, String namePlace);
	
	@Query(value="SELECT e FROM Event AS e WHERE e.state = 3 AND e.private_ = 0")
	List<Event> getPublicEvents();
}