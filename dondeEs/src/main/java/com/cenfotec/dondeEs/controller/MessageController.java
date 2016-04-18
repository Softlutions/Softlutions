package com.cenfotec.dondeEs.controller;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.contracts.MessageResponse;
import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.ejb.Message;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.repositories.ChatRepository;
import com.cenfotec.dondeEs.services.MessageServiceInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;
import com.cenfotec.dondeEs.utils.Utils;

@RestController
@RequestMapping(value = "rest/protected/message")
public class MessageController {

	@Autowired
	private MessageServiceInterface messageServiceInterface;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private UserServiceInterface userServiceInterface;

	@RequestMapping(value = "/createMessage", method = RequestMethod.POST)
	@Transactional
	public MessageResponse createMessage(@RequestBody Message message) {

		message.setTime(new Date());
		MessageResponse response = new MessageResponse();
		Boolean state = messageServiceInterface.saveMessage(message);

		if (state) {
			response.setCode(200);
		} else {
			response.setCode(500);
		}

		return response;
	}

	@RequestMapping(value = "/getAllMessageByChat/{id}", method = RequestMethod.GET)
	@Transactional
	public MessageResponse getAllMessageByChat(@PathVariable("id") int id) {
		MessageResponse response = new MessageResponse();

		response.setMessages(messageServiceInterface.getAllByChat(id));

		return response;
	}

	@RequestMapping(value = "insertImageChat", method = RequestMethod.POST)
	public MessageResponse insertImageChat(@RequestParam("idChat") int idChat, @RequestParam("idUser") int idUser,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam("content") String content) {
		MessageResponse response = new MessageResponse();
		Message m = new Message();
		if (file != null) {
			String image = Utils.writeToFile(file, servletContext);
			m.setImage(image);
		}
		Chat chat = chatRepository.findOne(idChat);
		User user = userServiceInterface.findById(idUser);
		m.setTime(new Date());
		if (content != null)
			m.setContent(content);
		m.setChat(chat);
		m.setUser(user);

		Boolean state = messageServiceInterface.saveMessage(m);

		if (state) {
			response.setCode(200);
		} else {
			response.setCode(500);
		}
		return response;
	}

}
