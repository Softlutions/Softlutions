package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.UserType;
import com.cenfotec.dondeEs.repositories.UserTypeRepository;

@Service
public class UserTypeService implements UserTypeServiceInterface {
	
	@Autowired private UserTypeRepository userTypeRepository;
	@Override
	public UserType findByName(String name) {
		UserType ut = userTypeRepository.findByName(name);
		return ut;
	}

}
