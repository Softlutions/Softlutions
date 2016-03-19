package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.services.UserServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/users")
public class UsersController {
	
	@Autowired private UserServiceInterface userServiceInterface;
	
	/**
	 * @author Ernesto Méndez A.
	 * @return Lista de usuarios y servicios
	 * @version 1.0
	 */
	@RequestMapping(value ="/getAll", method = RequestMethod.GET)
	public UserResponse getAll(){	
		UserResponse response = new UserResponse();
		response.setCode(200);
		response.setCodeMessage("users fetch success");
		response.setListUser(userServiceInterface.getAll());
		return response;
	}
	
	@RequestMapping(value ="/create", method = RequestMethod.POST)
	public UserResponse create(@RequestBody UserRequest ur){	
		UserResponse us = new UserResponse();
		Boolean state = userServiceInterface.saveUser(ur);
		if(state){
			us.setCode(200);
			us.setCodeMessage("User created succesfully");
		}
		return us;
	}
}
