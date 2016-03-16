package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.ejb.User;

public interface ChatRepository extends CrudRepository<Chat, Integer> {
	List<Chat> findAll();
	
	@Query("SELECT u FROM Event e join e.serviceContacts sc join sc.service s join s.user u where e.eventId = ?1")
	List<User> getUsersByEvent(int id);
	
	List<Chat> findByUsersUserId(int id);
}
