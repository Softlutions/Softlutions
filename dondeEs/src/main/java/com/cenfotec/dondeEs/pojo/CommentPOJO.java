package com.cenfotec.dondeEs.pojo;

 
import java.util.Date;


/**
 * The persistent class for the comment database table.
 * 
 */
public class CommentPOJO {


	private int commentId;

	private String content;
	
	private String image;

	private Date date;

	private EventParticipantPOJO eventParticipant;

	public CommentPOJO() {
	}

	public int getCommentId() {
		return this.commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public EventParticipantPOJO getEventParticipant() {
		return this.eventParticipant;
	}

	public void setEventParticipant(EventParticipantPOJO eventParticipant) {
		this.eventParticipant = eventParticipant;
	}

}