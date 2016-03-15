package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.ejb.User;

public interface ChatRepository extends CrudRepository<Chat, Integer> {
	List<Chat> findAll();
	
	@Query(value="SELECT u.user_id FROM user as u join event as e ON u.user_id = e.user_id join service as s where event_id = ?1", nativeQuery = true)
	List<User> getUsersByEvent(int id);
}
