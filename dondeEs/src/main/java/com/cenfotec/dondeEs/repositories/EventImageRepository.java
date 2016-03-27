package com.cenfotec.dondeEs.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.EventImage;

public interface EventImageRepository extends CrudRepository<EventImage, Integer> {
	List<EventImage> findAllByEventParticipantEventEventId(int eventId);
}
