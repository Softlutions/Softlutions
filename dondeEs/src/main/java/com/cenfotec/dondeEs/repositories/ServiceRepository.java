package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.*;

public interface ServiceRepository extends CrudRepository<Service, Integer> {

	List<Service> findAll();	
	
	
	
}
