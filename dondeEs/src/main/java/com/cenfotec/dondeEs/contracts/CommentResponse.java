package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.CommentPOJO;

public class CommentResponse extends BaseResponse {

	List<CommentPOJO> comments;

	public List<CommentPOJO> getComments() {
		return comments;
	}

	public void setComments(List<CommentPOJO> comments) {
		this.comments = comments;
	}
}
