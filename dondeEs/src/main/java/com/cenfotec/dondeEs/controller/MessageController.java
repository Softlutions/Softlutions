package com.cenfotec.dondeEs.controller;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.MessageResponse;
import com.cenfotec.dondeEs.ejb.Message;
import com.cenfotec.dondeEs.services.MessageServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/message")
public class MessageController {

	@Autowired private MessageServiceInterface messageServiceInterface;
	
	@RequestMapping(value ="/createMessage", method = RequestMethod.POST)
	@Transactional
	public MessageResponse createMessage (@RequestBody Message message){

		message.setTime(new Date());
		MessageResponse response = new MessageResponse();
		Boolean state = messageServiceInterface.saveMessage(message);
		
		if(state){
			response.setCode(200);
		}else{
			response.setCode(500);
		}
		
		return response;
	}
	

	@RequestMapping(value ="/getAllMessageByChat/{id}", method = RequestMethod.GET)
	@Transactional
	public MessageResponse getAllMessageByChat(@PathVariable("id") int id){
		MessageResponse response = new MessageResponse();
		
		response.setMessages(messageServiceInterface.getAllByChat(id));
		
		return response;
	}
	
}
