package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Message;
import com.cenfotec.dondeEs.pojo.MessagePOJO;
import com.cenfotec.dondeEs.repositories.MessageRepository;

import org.springframework.beans.BeanUtils;
@Service
public class MessageService implements MessageServiceInterface {
	
	@Autowired private MessageRepository messageRepository;

	@Override
	public Boolean saveMessage(Message message) {
		Message nmessage = messageRepository.save(message);
		return (nmessage == null) ? false : true ;
	}

	@Override
	public List<MessagePOJO> getAllByChat(int id) {
		List<Message> listMessage = messageRepository.findByChatChatId(id);
		List<MessagePOJO> messagePOJOs = new ArrayList<MessagePOJO>();
		listMessage.stream().forEach(m ->{
			MessagePOJO message = new MessagePOJO();
			BeanUtils.copyProperties(m, message);
			
			messagePOJOs.add(message);
		});
		return messagePOJOs;
	}
	
	
}
