package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cenfotec.dondeEs.ejb.Note;

public interface NoteRepository  extends CrudRepository<Note, Integer> {

	List<Note> findAll();
}