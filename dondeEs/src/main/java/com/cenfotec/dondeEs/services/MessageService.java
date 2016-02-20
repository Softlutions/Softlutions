package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.MessageRepository;

@Service
public class MessageService implements MessageServiceInterface {
	
	@Autowired private MessageRepository messageRepository;
}
