package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.ejb.User;

public interface UserServiceInterface {
	
	List<User> getAll();
	
	List<Service> getAllService(int idUser);

}
