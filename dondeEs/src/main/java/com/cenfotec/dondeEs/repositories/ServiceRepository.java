package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.*;

public interface ServiceRepository extends CrudRepository<Service, Integer> {

	List<Service> findAll();
	
	@Query("SELECT s FROM Service s WHERE s.state = 1 AND s.serviceCatalog.serviceCatalogId = ?1")
	List<Service> getByCatalogId(int catalogId);
	
}
