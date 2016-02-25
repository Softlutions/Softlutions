package com.cenfotec.dondeEs.controller;

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
import com.cenfotec.dondeEs.services.LoginServiceInterface;


/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value = "rest/login")
public class LoginController {
	
	@Autowired private LoginServiceInterface loginService;
	
	@RequestMapping(value = "/checkuser", method = RequestMethod.POST)
	@Transactional
	public BaseResponse checkuser(@RequestBody LoginRequest lr,HttpServletRequest servletRequest,HttpServletResponse servletResponse){	
		LoginResponse response = new LoginResponse();
		HttpSession currentSession = servletRequest.getSession();
		loginService.checkUser(lr,response,currentSession);
		
		return response;
		
	}
}
