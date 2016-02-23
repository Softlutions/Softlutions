package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.repositories.UserRepository;

@Service
public class UserService implements UserServiceInterface {
	@Autowired private UserRepository userRepository;
	
	public List<User> getAll(){
		List<User> user = userRepository.findAll();
		return user;
	}
}
