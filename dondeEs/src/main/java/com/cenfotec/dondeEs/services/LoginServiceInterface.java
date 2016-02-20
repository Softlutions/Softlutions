package com.cenfotec.dondeEs.services;

import javax.servlet.http.HttpSession;

import com.cenfotec.dondeEs.contracts.LoginRequest;
import com.cenfotec.dondeEs.contracts.LoginResponse;
import com.cenfotec.dondeEs.ejb.User;

public interface LoginServiceInterface {

	public void checkUser(LoginRequest lr, LoginResponse response, HttpSession currentSession);

}
