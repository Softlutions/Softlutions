package com.cenfotec.dondeEs.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.services.UserServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/user")
public class UserController {
	@Autowired private UserServiceInterface userServiceInterface;

	@RequestMapping(value ="/getAllService", method = RequestMethod.GET)
	public UserResponse getAllService(@PathParam("idUser") int idUser){
		UserResponse response = new UserResponse();
		response.setListService(userServiceInterface.getAllService(idUser));
		return response;
	}
}
