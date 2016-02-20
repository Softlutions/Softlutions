package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	List<User> findAll();
}