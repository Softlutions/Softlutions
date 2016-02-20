package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Log;

public interface LogRepository extends CrudRepository<Log, Integer>{
  List<Log> findAll();
}
