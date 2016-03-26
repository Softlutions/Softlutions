package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.contracts.EventImageResponse;
import com.cenfotec.dondeEs.services.EventImageServiceInterface;

@Controller
@RestController
@RequestMapping(value = "rest/protected/eventImage")
public class EventImageController {

	@Autowired
	private EventImageServiceInterface eventImageServiceInterface;

	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	public EventImageResponse saveImage(@RequestParam("eventParticipantId") int eventParticipantId, @RequestParam("file") MultipartFile file) {
		EventImageResponse response = new EventImageResponse();
		
		if(eventImageServiceInterface.saveImage(eventParticipantId, file)){
			response.setCode(200);
			response.setCodeMessage("Success");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		
		return response;
	}
	
	@RequestMapping(value = "/getAllByEventId/{id}", method = RequestMethod.GET)
	public EventImageResponse getAllByUser(@PathVariable("id") int id) {
		EventImageResponse response = new EventImageResponse();
		response.setCode(200);
		response.setCodeMessage("Success");
		response.setImages(eventImageServiceInterface.getAllByEventId(id));
		return response;
	}
}