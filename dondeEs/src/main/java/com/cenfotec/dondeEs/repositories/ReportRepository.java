package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.pojo.ParticipationOnEventsPOJO;

public interface ReportRepository{

//SELECT e FROM Event AS e JOIN e.eventParticipants AS ep GROUP BY ep.event HAVING e.state = 3 AND e.private_ = 0 AND e.publishDate > sysdate() ORDER BY count(ep.eventParticipantId) DESC")
	
/*	
	@Query(value = "SELECT count(acept) AS total, concat(u.name, ' ',u.last_name1, ' ',u.last_name2) AS userName " +
	"FROM user AS u "+
	"INNER JOIN service AS s ON s.user_id = u.user_id "+
	"INNER JOIN auction_services AS aus ON aus.service_id = s.service_id "+
	"WHERE u.user_id = 2 AND acept = 1", nativeQuery = true)
	List<ParticipationOnEventsPOJO> getParticipationOnEvents(); */
	
}
