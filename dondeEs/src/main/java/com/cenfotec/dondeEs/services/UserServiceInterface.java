package com.cenfotec.dondeEs.services;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.cenfotec.dondeEs.contracts.LoginRequest;
import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;

public interface UserServiceInterface {
	
	/**
	 * @param idUser Id del usuario
	 *            Id del usuario
	 * @return Lista de los servicios asociados al usuario
	 * @version 1.0
	 */
	List<ServicePOJO> getAllService(int idUser);
	
	public Boolean updateUser(User u);
	
	public int saveUser(UserRequest	 ur);
	
	User findByEmail(String email);

	/**
	 * @author Ernesto Méndez A.
	 * @return Lista de todos los usuarios registrados
	 * @version 1.0
	 */
	List<UserPOJO> getAll();
	
	/**
	 * @author Alejandro Bermúdez Vargas
	 * @exception AddressException no se encuentra la direccion de correo
	 * @exception MessagingException No encuentra el server.
	 * @param LoginRequest, tiene un atributo email del usuario
	 * @version 1.0
	 */
	Boolean updatePassword(LoginRequest ur);
	
	/**
	 * @author Alejandro Bermúdez Vargas
	 * @exception AddressException no se encuentra la direccion de correo
	 * @exception MessagingException No encuentra el server.
	 * @param LoginRequest, tiene un atributo email del usuario
	 * @version 1.0
	 */
	Boolean updatePasswordRequired(LoginRequest ur);
	
	/***
	 * Obtiene el usuario de cada servicio ofertado en todas las subastas de un
	 * determinado evento.
	 * 
	 * @author Enmanuel García González
	 * @version 1.0
	 */
	List<UserPOJO> getAllServicesProviderAuction(int idEvent);
	
	/***
	 * Obtiene un usuario por su id.
	 * 
	 * @author Enmanuel García González
	 * @version 1.0
	 */
	User findById(int id);
	
	/**
	 * @author Ernesto Méndez A.
	 * @param userId el usuario a modificar
	 * @param state el nuevo estado del usuario
	 * @return si la operacion fue exitosa
	 * @version 1.0
	 */
	Boolean changeUserState(int userId, boolean state);
	Boolean createUser(UserRequest ur);
}
