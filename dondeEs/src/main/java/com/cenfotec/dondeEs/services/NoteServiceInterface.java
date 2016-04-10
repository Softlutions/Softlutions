package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Note;
import com.cenfotec.dondeEs.pojo.NotePOJO;

public interface NoteServiceInterface {

	/**
	 * Obtiene todas las notas de recordatorio de un determinado evento.
	 * @author Enmanuel García González 
	 * @param id
	 * @return
	 */
	List<NotePOJO> getAllNoteByEvent(int id);

	Boolean saveNote(Note note);

	void delete(Note note);
}
