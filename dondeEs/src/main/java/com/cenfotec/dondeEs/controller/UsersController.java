package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.ejb.UserType;
import com.cenfotec.dondeEs.services.UserServiceInterface;
import com.cenfotec.dondeEs.services.UserTypeServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/users")
public class UsersController {
	
	@Autowired private UserServiceInterface userServiceInterface;
	@Autowired private UserTypeServiceInterface userTypeService;
	
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
	
	/**
	 * @author Juan Carlos Sánchez G.
	 * @param userId Id del usuario a consultar
	 * @return Usuario
	 * @version 1.0
	 */
	@RequestMapping(value ="/getUserById/{userId}", method = RequestMethod.GET)
	public UserResponse getUserById(@PathVariable("userId") int userId){	
		UserResponse response = new UserResponse();
		response.setUser(userServiceInterface.getUserById(userId));
		response.setCode(200);
		return response;
	}
	
	
	/**
	 * @author Ernesto Méndez A.
	 * @return resultado de la operacion
	 * @version 1.0
	 */
	@RequestMapping(value ="/changeState/{userId}/state/{state}", method = RequestMethod.GET)
	public UserResponse changeState(@PathVariable("userId") int userId, @PathVariable("state") boolean state){	
		UserResponse response = new UserResponse();
		
		if(userServiceInterface.changeUserState(userId, state)){
			response.setCode(200);
			response.setCodeMessage("success");
		}else{
			response.setCode(500);
			response.setCodeMessage("internal error");
		}
		
		return response;
	}
	
	@RequestMapping(value ="/create", method = RequestMethod.POST)
	public UserResponse create(@RequestBody UserRequest ur){	
		UserResponse us = new UserResponse();
		Boolean userId= userServiceInterface.createUser(ur);
		if(userId){
			us.setCode(200);
			us.setCodeMessage("User created succesfully");
		}else{
			us.setCode(400);
			us.setCodeMessage("El usuario ya existe en la base de datos!");
		}
		
		return us;
	}
	
	@RequestMapping(value ="/updateUser", method = RequestMethod.PUT)
	public UserResponse updateUser(@RequestBody User ur){	
		UserResponse us = new UserResponse();
		User nur = ur;		
		UserType ust = userTypeService.findByName(ur.getUserType().getName());
		nur.setUserType(ust);
		Boolean userId= userServiceInterface.updateUser(nur);
		if(userId){
			us.setCode(200);
			us.setCodeMessage("User update succesfully");
		}else{
			us.setCode(400);
			us.setCodeMessage("El usuario ya existe en la base de datos!");
		}
		
		return us;
	}
	
	/**
	 * Lista el nombre y el id de todos los prestatarios registrados.
	 * @author Enmanuel García González.
	 * @return Lista de pretatarios con su id y nombre.
	 * @version 1.0
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value ="/getAllServiceProviderNames", method = RequestMethod.GET)
	public UserResponse getAllServiceProviderNames(){	
		UserResponse response = new UserResponse();
		
		try {
			response.setListUser(userServiceInterface.getAllServiceProviderNames());																							 
			response.setCode(200);
			
		} catch (Exception e) {
			response.setCode(500);
			response.setCodeMessage(e.toString());
			e.printStackTrace();

		} finally { return response; }
	}
}
