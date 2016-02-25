package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.repositories.UserRepository;

@org.springframework.stereotype.Service
public class UserService implements UserServiceInterface {
	@Autowired private UserRepository userRepository;
	
	@Override
	public List<User> getAll(){
		List<User> user = userRepository.findAll();
		return user;
	}
	
	@Override
	public List<com.cenfotec.dondeEs.ejb.Service> getAllService(int idUser){
		List<com.cenfotec.dondeEs.ejb.Service> listService = userRepository.getServicesByUser(idUser);
		
		return listService;
	}
}
