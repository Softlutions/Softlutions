package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ServicePOJO;

public interface UserServiceInterface {
	
	List<User> getAll();
	
	List<ServicePOJO> getAllService(int idUser);

}
