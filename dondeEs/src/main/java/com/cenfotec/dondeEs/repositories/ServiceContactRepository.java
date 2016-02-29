package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.ServiceContact;;

public interface ServiceContactRepository extends CrudRepository<ServiceContact, Integer> {

	List<ServiceContact> findAll();
	
	@Query("SELECT sc FROM ServiceContact sc JOIN sc.event e WHERE e.eventId = ?1")
	List<ServiceContact> findServiceContactByEventId(int eventId);
	
}
