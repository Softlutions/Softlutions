package com.cenfotec.dondeEs.services;

import com.cenfotec.dondeEs.ejb.UserType;

public interface UserTypeServiceInterface {

	UserType findByName(String name);
}
