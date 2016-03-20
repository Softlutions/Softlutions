package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Note;

public interface NoteServiceInterface {

	/**
	 * @author Enmanuel García González 
	 * @param id
	 * @return
	 */
	List<Note> getAllNoteByEvent(int id);

	Boolean saveNote(Note note);
}
