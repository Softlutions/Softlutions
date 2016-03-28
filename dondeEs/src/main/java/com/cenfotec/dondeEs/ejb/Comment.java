package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the comment database table.
 * 
 */
@Entity
@Table(name="comment")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="comment_id")
	private int commentId;

	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String image;

	//bi-directional many-to-one association to EventParticipant
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="event_participant_id")
	private EventParticipant eventParticipant;

	public Comment() {
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

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public EventParticipant getEventParticipant() {
		return this.eventParticipant;
	}

	public void setEventParticipant(EventParticipant eventParticipant) {
		this.eventParticipant = eventParticipant;
	}

}