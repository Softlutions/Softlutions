package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Note;
import com.cenfotec.dondeEs.repositories.NoteRepository;

@Service
public class NoteService implements NoteServiceInterface {
	@Autowired private NoteRepository noteRepository;

	@Override
	public Boolean saveNote(Note note) {
		// TODO Auto-generated method stub
		Note nnote = noteRepository.save(note);
		return (nnote == null) ? false : true;
	}
}
