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
import com.cenfotec.dondeEs.contracts.RoleResponse;
import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.services.LoginServiceInterface;
import com.cenfotec.dondeEs.services.RoleServiceInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;

/**
 * Handles requests for the application home page.
 */
@RestController
@RequestMapping(value = "rest/login")
public class LoginController {

	@Autowired
	private LoginServiceInterface loginService;
	@Autowired
	private UserServiceInterface userServiceInterface;
	@Autowired
	private RoleServiceInterface roleServiceInterface;

	/**
	 * @Author Ernesto Méndez A.
	 * @param lr
	 *            Petición que contiene el email y la contraseña del usuarioa
	 *            logear
	 * @param servletRequest
	 *            Petición por defecto al server
	 * @param servletResponse
	 *            Canal de respuesta (puede ser nulo)
	 * @return Respuesta que contiene los datos del usuario logueado
	 * @version 1.0
	 */
	@RequestMapping(value = "/checkuser", method = RequestMethod.POST)
	@Transactional
	public BaseResponse checkuser(@RequestBody LoginRequest lr, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {
		LoginResponse response = new LoginResponse();
		HttpSession currentSession = servletRequest.getSession();
		loginService.checkUser(lr, response, currentSession);

		return response;
	}

	/**
	 * @author Ernesto Méndez A.
	 * @param servletRequest
	 *            sesion actual
	 * @return resultado de la operacion, si el cierre de sesion fue
	 *         satisfactorio o no
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@Transactional
	public BaseResponse logout(HttpServletRequest servletRequest) {
		servletRequest.getSession().invalidate();
		BaseResponse response = new BaseResponse();
		response.setCode(200);
		response.setCodeMessage("successfully logout");
		return response;
	}

	/**
	 * @author Alejandro Bermúdez Vargas
	 * @param LoginRequest,
	 *            Este objeto poosee un atributo email del usuario.
	 * @version 1.0
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@Transactional
	public UserResponse updatePassword(@RequestBody LoginRequest lr, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {
		UserResponse us = new UserResponse();
		if (userServiceInterface.updatePassword(lr)) {
			us.setCode(200);
			us.setCodeMessage("Password cambiado exitosamente");
		}
		return us;
	}
	
	/**
	 * @author Alejandro Bermúdez Vargas
	 * @param LoginRequest,
	 *            Este objeto poosee un atributo email y password
	 * @version 1.0
	 */
	@RequestMapping(value = "/updatePasswordRequired", method = RequestMethod.POST)
	@Transactional
	public UserResponse updatePasswordRequired(@RequestBody LoginRequest lr, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {
		UserResponse us = new UserResponse();
		if (userServiceInterface.updatePasswordRequired(lr)) {
			us.setCode(200);
			us.setCodeMessage("Password cambiado exitosamente");
		}
		return us;
	}

	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional
	public UserResponse create(@RequestBody UserRequest ur, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {
		UserResponse us = new UserResponse();
		ur.getUser().setState(true);
		Boolean state = userServiceInterface.createUser(ur);
		if (state) {
			us.setCode(200);
			us.setCodeMessage("Usuario creado correctamente!");
			return us;
		}
		us.setCode(400);
		us.setCodeMessage("El usuario ya existe en la base de datos!");
		return us;
	}

	// get all
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public RoleResponse getAll() {
		RoleResponse response = new RoleResponse();
		response.setCode(200);
		response.setCodeMessage("Role list success");
		response.setListRole(roleServiceInterface.getAll());
		return response;
	}
}
