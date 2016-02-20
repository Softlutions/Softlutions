package com.cenfotec.dondeEs.services;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cenfotec.dondeEs.contracts.LoginRequest;
import com.cenfotec.dondeEs.contracts.LoginResponse;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.repositories.LoginRepository;

@Service
public class LoginService implements LoginServiceInterface{

	@Autowired private LoginRepository loginRepository;
	
	@Override
	@Transactional
	public void checkUser(LoginRequest lr, LoginResponse response, HttpSession currentSession) {
		User loggedUser = loginRepository.findByEmailAndPassword(lr.getEmail(), lr.getPassword());
		if(loggedUser == null){
			response.setCode(401);
			response.setErrorMessage("Unauthorized User");
		}else{
			response.setCode(200);
			response.setCodeMessage("User authorized");
			
			//CREATE AND SET THE VALUES FOR THE CONTRACT OBJECT
			response.setIdUsuario(loggedUser.getUserId());
			response.setFirstName(loggedUser.getName());
			response.setLastName(loggedUser.getLastName1());
			//
			currentSession.setAttribute("idUser", loggedUser.getUserId());
		}
	}		
}