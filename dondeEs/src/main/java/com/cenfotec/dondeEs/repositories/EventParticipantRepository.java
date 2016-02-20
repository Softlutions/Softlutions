package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.EventParticipant;

public interface EventParticipantRepository extends CrudRepository<EventParticipant, Integer> {

	List<EventParticipant> findAll();
}
