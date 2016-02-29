package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.pojo.UserPOJO;

import com.cenfotec.dondeEs.pojo.ServicePOJO;

public interface UserServiceInterface {
	
	List<ServicePOJO> getAllService(int idUser);
	
	List<UserPOJO> getAll();
}
