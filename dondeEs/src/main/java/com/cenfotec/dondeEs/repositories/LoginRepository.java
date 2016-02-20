package com.cenfotec.dondeEs.repositories;

import org.springframework.data.repository.CrudRepository;


import com.cenfotec.dondeEs.ejb.User;

public interface LoginRepository extends CrudRepository<User,Integer> {
	
	public static final int PAGE_SIZE = 5;
	
	User findByEmailAndPassword(String email, String password);
}
