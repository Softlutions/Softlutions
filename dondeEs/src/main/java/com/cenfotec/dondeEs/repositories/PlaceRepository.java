package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.Place;


public interface PlaceRepository extends CrudRepository<Place, Integer> {
	List<Place> findAll();
}
