package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import com.cenfotec.dondeEs.ejb.Permission;

public interface PermissionRepository extends CrudRepository<Permission,Integer> {
	List<Permission> findAll();
}
