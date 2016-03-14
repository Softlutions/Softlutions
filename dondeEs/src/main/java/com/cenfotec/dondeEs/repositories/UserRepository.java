package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.ejb.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	List<User> findAll();
	
	User findByEmail(String email);
	
	@Query("SELECT s FROM Service as s join s.user as u WHERE u.userId = ?1 ")
	List<Service> getServicesByUser(int id);
	
	User findByUserId(int id);
}