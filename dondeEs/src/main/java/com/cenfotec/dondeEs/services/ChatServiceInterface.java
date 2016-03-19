package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.pojo.ChatPOJO;

public interface ChatServiceInterface {

	Boolean saveChat(Chat chat);
	
	void saveChatByEvent(int idEvent);
	
	List<ChatPOJO> getAllByUser(int idUser);
}
