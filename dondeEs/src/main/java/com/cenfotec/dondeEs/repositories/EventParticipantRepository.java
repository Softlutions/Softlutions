package com.cenfotec.dondeEs.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.EventParticipant;

public interface EventParticipantRepository extends CrudRepository<EventParticipant, Integer> {

	List<EventParticipant> findAll();

	@Query("SELECT ep FROM EventParticipant ep JOIN ep.event e WHERE e.eventId = ?1")
	List<EventParticipant> findEventParticipantByEventId(int EventId);
	
	EventParticipant findByEventParticipantId(int id);

	EventParticipant findByUserUserIdAndEventEventId(int userId, int eventId);
}
