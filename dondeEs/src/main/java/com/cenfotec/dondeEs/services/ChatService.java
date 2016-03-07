package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.ChatRepository;

@Service
public class ChatService implements ChatServiceInterface {
	@Autowired private ChatRepository chatRepository;
}
