package com.cenfotec.dondeEs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.RoleResponse;
import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.services.RoleServiceInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/role")
public class RoleController {
	
	@Autowired private RoleServiceInterface roleServiceInterface;
	@Autowired private HttpServletRequest request;
	
	
	//	get all
	@RequestMapping(value ="/getAll", method = RequestMethod.GET)
	public RoleResponse getAll(){	
		RoleResponse response = new RoleResponse();
		response.setCode(200);
		response.setCodeMessage("users fetch success");
		response.setListRole(roleServiceInterface.getAll());
		return response;
	}
}