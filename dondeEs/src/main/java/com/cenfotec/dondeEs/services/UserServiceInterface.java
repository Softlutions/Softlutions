package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.pojo.UserPOJO;

import com.cenfotec.dondeEs.pojo.ServicePOJO;

public interface UserServiceInterface {
	
	/**
	 * @param idUser Id del usuario
	 * @return Lista de los servicios asociados al usuario
	 * @version 1.0
	 */
	List<ServicePOJO> getAllService(int idUser);
	
	public Boolean saveUser(UserRequest	 ur);
	
	User findByEmail(String email);

	/**
	 * @author Ernesto Méndez A.
	 * @return Lista de todos los usuarios registrados
	 * @version 1.0
	 */
	List<UserPOJO> getAll();

	List<UserPOJO> getAllServicesProviderAuction(int idEvent);
}
