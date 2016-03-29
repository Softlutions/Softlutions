package com.cenfotec.dondeEs.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

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
		
		if(commentServiceInterface.saveComment(comment, file)){
			response.setCode(200);
			response.setCodeMessage("Succesful");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		
		return response;
	}
	
	/**
	 * @author Juan Carlos Sánchez G. (Autor)
	 * @param comment Peticion que contiene la información del comentario por crear.
	 * @return response Respuesta del servidor de la petición.
	 * @version 1.0
	 */
	@RequestMapping(value ="/createComment", method = RequestMethod.POST)
	public CommentResponse createComment(@RequestBody Comment comment){
		CommentResponse response = new CommentResponse();
		Boolean state = commentServiceInterface.saveComment(comment);
		
		if(state){
			response.setCode(200);
			response.setCodeMessage("Succesfull");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		
		return response;
	}
	
	/**
	 * @author Juan Carlos Sánchez G. (Autor)
	 * @param comment Peticion que contiene la información del comentario por crear.
	 * @return response Respuesta del servidor de la petición.
	 * @version 1.0
	 */
	@RequestMapping(value ="/getCommentsByEvent/{eventId}", method = RequestMethod.GET)
	@Transactional
	public CommentResponse getCommentsByEvent(@PathVariable("eventId") int eventId){
		CommentResponse response = new CommentResponse();
		response.setCommentList(commentServiceInterface.getCommentsByEvent(eventId));

		return response;
	}
}
