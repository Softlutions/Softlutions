package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.cenfotec.dondeEs.ejb.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
	List<Comment> findAll();

	@Query("select c from Comment c join c.eventParticipant ep join ep.event e where e.eventId = ?1")
	List<Comment> getAllByEventId(int eventId);
}
