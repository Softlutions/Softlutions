package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.UserPOJO;

import com.cenfotec.dondeEs.pojo.ServicePOJO;

public interface UserServiceInterface {
	
	List<ServicePOJO> getAllService(int idUser);
	public Boolean saveUser(UserRequest	 ur);
	
	User findByEmail(String email);
	List<UserPOJO> getAll();
	
	List<UserPOJO> getAllServicesProviderAuction(int idEvent);
}
