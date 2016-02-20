package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.ServiceContact;;

public interface ServiceContactRepository extends CrudRepository<ServiceContact, Integer> {

	List<ServiceContact> findAll();
}
