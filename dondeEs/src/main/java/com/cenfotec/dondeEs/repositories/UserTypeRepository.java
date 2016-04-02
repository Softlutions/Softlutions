package com.cenfotec.dondeEs.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.UserType;;

public interface UserTypeRepository extends CrudRepository<UserType, Integer> {

	UserType findByName(String name);
}
