package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.ejb.User;

public interface ChatRepository extends CrudRepository<Chat, Integer> {
	
	List<Chat> findAll();
	
	@Query("SELECT u FROM Event e "
			+ "JOIN e.serviceContacts sc "
			+ "JOIN sc.service s "
			+ "JOIN s.user u "
			+ "WHERE u.state = 1 AND sc.state = 1 AND e.eventId = ?1")
	List<User> getUsersByEvent(int id);
	
	List<Chat> findByUsersUserId(int id);
}
