package com.cenfotec.dondeEs.services;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.pojo.CommentPOJO;
import com.cenfotec.dondeEs.repositories.CommentRepository;
import com.cenfotec.dondeEs.utils.Utils;

@Service
public class CommentService implements CommentServiceInterface {
	@Autowired private CommentRepository commentRepository;
	@Autowired private ServletContext servletContext;
	
	@Override
	public Boolean saveComment(Comment comment) {
		return saveComment(comment, null);
	}
	
	@Override
	public Boolean saveComment(Comment comment, MultipartFile file) {
		String image = null;
		
		if(file != null)
			image = Utils.writeToFile(file, servletContext);
		
		comment.setImage(image);
		Comment ncomment = commentRepository.save(comment);
		return (ncomment == null);
	}
	
	@Override
	public List<CommentPOJO> getAllByEvent(int eventId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}