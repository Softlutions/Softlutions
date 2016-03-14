package com.cenfotec.dondeEs.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.BaseResponse;
import com.cenfotec.dondeEs.contracts.LoginRequest;
import com.cenfotec.dondeEs.contracts.LoginResponse;
import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.services.LoginServiceInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;


/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value = "rest/login")
public class LoginController {
	
	@Autowired private LoginServiceInterface loginService;
	@Autowired private UserServiceInterface userServiceInterface;
	/**
	 * @Author Ernesto Méndez A.
	 * @param lr Petición que contiene el email y la contraseña del usuarioa logear
	 * @param servletRequest Petición por defecto al server
	 * @param servletResponse Canal de respuesta (puede ser nulo)
	 * @return Respuesta que contiene los datos del usuario logueado
	 * @version 1.0
	 */
	@RequestMapping(value = "/checkuser", method = RequestMethod.POST)
	@Transactional
	public BaseResponse checkuser(@RequestBody LoginRequest lr,HttpServletRequest servletRequest,HttpServletResponse servletResponse){	
		LoginResponse response = new LoginResponse();
		HttpSession currentSession = servletRequest.getSession();
		loginService.checkUser(lr,response,currentSession);
		
		return response;
		
	}
	
	
	
	/**
	 * @author Alejandro Bermúdez Vargas
	 * @param LoginRequest, Este objeto poosee un atributo email del usuario.
	 * @version 1.0
	 */
	@RequestMapping(value ="/updatePassword", method = RequestMethod.POST)
	public UserResponse updatePassword(@RequestBody LoginRequest lr,HttpServletRequest servletRequest,HttpServletResponse servletResponse){	
		UserResponse us = new UserResponse();
		if(userServiceInterface.updatePassword(lr)){
			us.setCode(200);
			us.setCodeMessage("Password cambiado exitosamente");
		}else{
			us.setCode(401);
			us.setCodeMessage("No se pudo cambiar la contraseña");
		}
		return us;
	}
}
