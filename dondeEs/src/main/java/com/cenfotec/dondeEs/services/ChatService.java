package com.cenfotec.dondeEs.services;


import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ChatPOJO;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.repositories.ChatRepository;
import com.cenfotec.dondeEs.repositories.EventRepository;
import com.cenfotec.dondeEs.repositories.UserRepository;

import org.springframework.beans.BeanUtils;

@Service
public class ChatService implements ChatServiceInterface {
	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EventRepository eventRepository;
	
	@Override
	@Transactional
	public Boolean saveChat(Chat nchat) {
		Event aux = eventRepository.findOne(nchat.getEvent().getEventId());
		List<User> users = chatRepository.getUsersByEvent(nchat.getEvent().getEventId());
		
		if(users.size() > 0){
			nchat.setEvent(aux);
			Chat chat = chatRepository.save(nchat);
			users.add(aux.getUser());
			
			for(User nUser : users){
				if(nUser.getChats() == null){
					nUser.setChats(new ArrayList<Chat>());
				}
				nUser.getChats().add(chat);
				userRepository.save(nUser); 
			}
		}
		
		return (nchat == null) ? false : true;
	}


	@Override
	public List<ChatPOJO> getAllByUser(int idUser) {
		List<Chat> chatRep = chatRepository.findByUsersUserId(idUser);
		List<ChatPOJO> chatsPOJO = new ArrayList<ChatPOJO>();
		chatRep.stream().forEach(c -> {
			ChatPOJO chatPOJO = new ChatPOJO();
			BeanUtils.copyProperties(c, chatPOJO);
			chatPOJO.setUsers(null);
			chatPOJO.setMessages(null);
			if(c.getEvent() != null){
				EventPOJO eventPOJO = new EventPOJO();
				BeanUtils.copyProperties(c.getEvent(), eventPOJO);
				eventPOJO.setChats(null);
				eventPOJO.setEventParticipants(null);
				eventPOJO.setUser(null);
				eventPOJO.setServiceContacts(null);
				eventPOJO.setNotes(null);
				chatPOJO.setEvent(eventPOJO);
			}
			chatsPOJO.add(chatPOJO);
		});
		
		return chatsPOJO;
	}


	@Override
	public void saveChatByEvent(int idEvent) {
		Chat nchat = new Chat();
		
		Event nevent = eventRepository.findOne(idEvent);
		
		nchat.setEvent(nevent);
		
		nevent.setState((byte)2);
		
		eventRepository.save(nevent);
		
		saveChat(nchat);
		
	}
	
}
