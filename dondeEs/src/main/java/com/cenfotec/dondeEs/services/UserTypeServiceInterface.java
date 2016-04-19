package com.cenfotec.dondeEs.services;

import com.cenfotec.dondeEs.ejb.UserType;

public interface UserTypeServiceInterface {
	
	UserType findById(int id);
	UserType findByName(String name);
}
