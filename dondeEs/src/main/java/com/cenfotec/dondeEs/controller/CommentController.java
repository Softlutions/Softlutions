package com.cenfotec.dondeEs.controller;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.cenfotec.dondeEs.contracts.CommentResponse;
import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.services.CommentServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/comment")
public class CommentController {
	
	@Autowired private CommentServiceInterface commentServiceInterface;
	
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
