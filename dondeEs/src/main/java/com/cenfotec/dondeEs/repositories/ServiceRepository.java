package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.*;

public interface ServiceRepository extends CrudRepository<Service, Integer> {

	List<Service> findAll();
	
//	@Query("SELECT s FROM Service as s join s.user as u WHERE u.userId = ?1 ")
	@Query(value = "SELECT * FROM service as s join service_contact as sc on s.service_id = sc.service_id join event as e on sc.event_id = e.event_id join user as u ON e.user_id = u.user_id WHERE u.user_id = ?1", nativeQuery = true)
	List<Service> getService(int id);
	
}
