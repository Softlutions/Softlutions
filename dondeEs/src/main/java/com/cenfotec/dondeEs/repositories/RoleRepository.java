package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Role;

public interface RoleRepository extends CrudRepository<Role,Integer>{
	List<Role> findAll();

}
