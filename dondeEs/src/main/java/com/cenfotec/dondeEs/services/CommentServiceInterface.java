package com.cenfotec.dondeEs.services;

import java.util.List;
import com.cenfotec.dondeEs.ejb.Comment;
import com.cenfotec.dondeEs.pojo.CommentPOJO;

public interface CommentServiceInterface {

	Boolean saveComment(Comment comment);

	List<CommentPOJO> getCommentsByEvent(int eventId);
}
