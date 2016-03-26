package com.cenfotec.dondeEs.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.contracts.CommentResponse;
import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.services.CommentServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/comment")
public class CommentController {
	
	@Autowired private CommentServiceInterface commentServiceInterface;
	
	@RequestMapping(value ="/saveComment", method = RequestMethod.POST)
	public CommentResponse saveComment(@RequestParam("content") String content, @RequestParam(value="file", required=false) MultipartFile file){
		CommentResponse response = new CommentResponse();
		
		Comment comment = new Comment();
		comment.setDate(new Date());
		comment.setContent(content);
		
		Boolean state = commentServiceInterface.saveComment(comment, file);
		
		if(state){
			response.setCode(200);
			response.setCodeMessage("Succesfull");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		
		return response;
	}
	
	@RequestMapping(value ="/getAllByEvent/{eventId}", method = RequestMethod.GET)
	public CommentResponse saveComment(@PathVariable("eventId") int eventId){
		CommentResponse response = new CommentResponse();
		response.setComments(commentServiceInterface.getAllByEvent(eventId));
		
		if(response.getComments() != null){
			response.setCode(200);
			response.setCodeMessage("Succesfull");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		
		return response;
	}
}
