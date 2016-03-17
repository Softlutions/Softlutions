package com.cenfotec.dondeEs.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.ChatResponse;
import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.services.ChatServiceInterface;;

@RestController
@RequestMapping(value = "rest/protected/chat")
public class ChatController {

	@Autowired private ChatServiceInterface chatServiceInterface;
	
	@RequestMapping(value ="/createChatUser", method = RequestMethod.POST)
	@Transactional
	public ChatResponse createChatUser(@RequestBody Chat nchat){
		ChatResponse chatResponse = new ChatResponse();
		
		Boolean state = chatServiceInterface.saveChat(nchat);
		
		if(state){
			chatResponse.setCode(200);
		}else{
			chatResponse.setCode(500);
		}
		
		return chatResponse;
	}
	
	@RequestMapping(value ="/saveChatEventId/{id}", method = RequestMethod.GET)
	@Transactional
	public ChatResponse saveChatEventId(@PathVariable ("id") int id){
		ChatResponse response = new ChatResponse();
		
		chatServiceInterface.saveChatByEvent(id);
		
		response.setCode(200);
		
		return response;
	}
	
	@RequestMapping(value ="/getChatsByUser/{id}", method = RequestMethod.GET)
	@Transactional
	public ChatResponse getChatsByUser(@PathVariable ("id") int id){
		ChatResponse response = new ChatResponse();
		
		response.setChats(chatServiceInterface.getAllByUser(id));
		
		return response;
	}
}
