package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.PasswordHistory;;

public interface PasswordHistoryRepository extends CrudRepository<PasswordHistory, Integer> {

	List<PasswordHistory> findAll();
}
