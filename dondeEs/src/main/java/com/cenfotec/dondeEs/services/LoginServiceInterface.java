package com.cenfotec.dondeEs.services;

import javax.servlet.http.HttpSession;

import com.cenfotec.dondeEs.contracts.LoginRequest;
import com.cenfotec.dondeEs.contracts.LoginResponse;

public interface LoginServiceInterface {

	/**
	 * @author Ernesto Méndez A.
	 * @param lr Petición de login
	 * @param response Respuesta con el estaddo del login y los datos del usuario logeado
	 * @param currentSession Sesión actual
	 * @version 1.0
	 */
	public void checkUser(LoginRequest lr, LoginResponse response, HttpSession currentSession);
}
