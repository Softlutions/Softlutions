package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.pojo.CommentPOJO;
import com.cenfotec.dondeEs.repositories.CommentRepository;
import com.cenfotec.dondeEs.utils.Utils;

import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.pojo.OfflineUserPOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.repositories.EventParticipantRepository;

@Service
public class CommentService implements CommentServiceInterface {

	@Autowired private EventParticipantRepository eventParticipantRepository;
	@Autowired private CommentRepository commentRepository;
	@Autowired private ServletContext servletContext;
	
	@Override
	public Boolean saveComment(Comment comment, MultipartFile file) {
		String image = null;
		Comment ncomment = null;
		
		if(file != null)
			image = Utils.writeToFile(file, servletContext);
		
		if(image != null){
			comment.setImage(image);
			ncomment = commentRepository.save(comment);
		}
		
		return (ncomment == null);
	}
	
	@Override
	public Boolean saveComment(Comment comment) {
		comment.setEventParticipant(eventParticipantRepository.findOne(comment.getEventParticipant().getEventParticipantId()));
		Comment ncomment = commentRepository.save(comment);
		return (ncomment == null);
	}
	
	@Override
	public List<CommentPOJO> getCommentsByEvent(int eventId) {
		List<Comment> commentList = commentRepository.getAllByEventId(eventId);
		List<CommentPOJO> commentPOJOList = new ArrayList<CommentPOJO>();
		commentList.stream().forEach(c ->{
			CommentPOJO commentPOJO = new CommentPOJO();
			commentPOJO.setCommentId(c.getCommentId());
			commentPOJO.setContent(c.getContent());
			commentPOJO.setDate(c.getDate());
			EventParticipantPOJO eventParticipantPOJO = new EventParticipantPOJO();
			if(c.getEventParticipant().getUser() != null){
				UserPOJO userPOJO = new UserPOJO();
				userPOJO.setName(c.getEventParticipant().getUser().getName());
				userPOJO.setLastName1(c.getEventParticipant().getUser().getLastName1());
				userPOJO.setLastName2(c.getEventParticipant().getUser().getLastName2());
				eventParticipantPOJO.setUser(userPOJO);
			}
			if(c.getEventParticipant().getOfflineUser() != null){
				OfflineUserPOJO offlineUserPOJO = new OfflineUserPOJO();
				offlineUserPOJO.setEmail(c.getEventParticipant().getOfflineUser().getEmail());
				eventParticipantPOJO.setOfflineUser(offlineUserPOJO);
			}
			commentPOJO.setEventParticipant(eventParticipantPOJO);
			commentPOJOList.add(commentPOJO);
			
		});
		return commentPOJOList;
	}
	
	
}