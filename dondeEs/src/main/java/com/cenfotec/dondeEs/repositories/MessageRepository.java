package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {

	List<Message> findAll();
	
	List<Message> findByChatChatId(int id);
}
