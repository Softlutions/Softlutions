package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Message;
import com.cenfotec.dondeEs.pojo.MessagePOJO;

public interface MessageServiceInterface {

	Boolean saveMessage(Message message);
	
	List<MessagePOJO> getAllByChat(int id);
}
