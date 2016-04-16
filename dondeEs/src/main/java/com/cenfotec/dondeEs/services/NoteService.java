package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cenfotec.dondeEs.ejb.Note;
import com.cenfotec.dondeEs.pojo.NotePOJO;
import com.cenfotec.dondeEs.repositories.EventRepository;
import com.cenfotec.dondeEs.repositories.NoteRepository;

@Service
public class NoteService implements NoteServiceInterface {
	
	@Autowired private EventRepository eventRepository;
	@Autowired private NoteRepository noteRepository;

	@Override
	public Boolean saveNote(Note note) {
		Note nnote = noteRepository.save(note);
		return (nnote == null) ? false : true;
	}

	@Override
	@Transactional
	public List<NotePOJO> getAllNoteByEvent(int idEvent) {
		List<Note> notes = noteRepository.findAllByEventEventId(idEvent);
		List<NotePOJO> notesPojo = new ArrayList<NotePOJO>();
		notes.stream().forEach(n -> {
			NotePOJO notePojo = new NotePOJO();	
			notePojo.setContent(n.getContent());
			notePojo.setDate(n.getDate());
			notePojo.setNoteId(n.getNoteId());
			notePojo.setState(n.getState());
			notePojo.setEvent(null);
			
			notesPojo.add(notePojo);
		});
		
		return notesPojo;
	}

	@Override
	public void delete(Note note) {
		note.setEvent(eventRepository.findOne(note.getEvent().getEventId()));
		noteRepository.delete(note);
		
	}
}
