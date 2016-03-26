package com.cenfotec.dondeEs.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.NoteResponse;
import com.cenfotec.dondeEs.ejb.Note;
import com.cenfotec.dondeEs.services.NoteServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/note")
public class NoteController {

	@Autowired NoteServiceInterface noteServiceInterface;
	
	@RequestMapping(value = "/createNote", method= RequestMethod.POST )
	public NoteResponse createNote(@RequestBody Note note){
	
		NoteResponse response = new NoteResponse();
		
		note.setDate(new Date());
		Boolean state = noteServiceInterface.saveNote(note);
		
		if(state){
			response.setCode(200);
		}else{
			response.setCode(500);
		}
		
		return response;
	}
	
	/**
	 * Actualiza la información de un determinado evento.
	 * @author Enmanuel García González 
	 * @param note
	 * @return 
	 * @version 1.0
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/updateNote", method= RequestMethod.PUT )
	public NoteResponse updateNote(@RequestBody Note note){
		NoteResponse response = new NoteResponse();
		
		try {
			note.setDate(new Date());
			Boolean state = noteServiceInterface.saveNote(note);
			
			if(state){
				response.setCode(200);
			}else{
				response.setCode(500);
			}
			
		} catch (Exception e) {
			response.setCode(500);
			
		} finally { return response; }
	}
	
	/**
	 * Obtiene todas las notas de recordatorio de un determinado evento.
	 * @author Enmanuel García González 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getAllNoteByEvent/{id}", method = RequestMethod.GET)
	public NoteResponse getAllNoteByEvent(@PathVariable("id") int id) {
		NoteResponse response = new NoteResponse();
		
		try {
			response.setCode(200);
			response.setCodeMessage("Fetch all notes by event");
			response.setNotes(noteServiceInterface.getAllNoteByEvent(id));
			
		} catch (Exception e) {
			response.setCode(500);
			
		} finally { return response; }
		
	}
}
