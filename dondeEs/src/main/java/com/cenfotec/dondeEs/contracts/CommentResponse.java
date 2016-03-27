package com.cenfotec.dondeEs.contracts;

import java.util.List;
import com.cenfotec.dondeEs.pojo.CommentPOJO;

public class CommentResponse extends BaseResponse{

	private CommentPOJO comment;
	private List<CommentPOJO> commentList;
	
	public CommentPOJO getComment() {
		return comment;
	}
	
	public void setComment(CommentPOJO comment) {
		this.comment = comment;
	}
	
	public List<CommentPOJO> getCommentList() {
		return commentList;
	}
	
	public void setCommentList(List<CommentPOJO> commentList) {
		this.commentList = commentList;
	}
}
