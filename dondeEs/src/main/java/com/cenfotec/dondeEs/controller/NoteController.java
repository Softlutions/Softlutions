package com.cenfotec.dondeEs.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
}
