package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.pojo.CommentPOJO;

public interface CommentServiceInterface {

	Boolean saveComment(Comment comment);
	Boolean saveComment(Comment comment, MultipartFile file);
	List<CommentPOJO> getAllByEvent(int eventId);
}
