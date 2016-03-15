package com.cenfotec.dondeEs.services;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.repositories.ChatRepository;

@Service
public class ChatService implements ChatServiceInterface {
	@Autowired
	private ChatRepository chatRepository;
	
	
	
	@Override
	@Transactional
	public Boolean saveChat(Chat nchat) {
		List<User> users = chatRepository.getUsersByEvent(nchat.getEvent().getEventId());	
		nchat.setUsers(users);
//		List<Chat> chats = new ArrayList<Chat>();
//		chats.add(nchat);
		Chat chat = chatRepository.save(nchat);
		return (chat == null) ? false : true;
	}
}
