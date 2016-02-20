package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.Chat;

public interface ChatRepository extends CrudRepository<Chat, Integer> {
	List<Chat> findAll();
}
