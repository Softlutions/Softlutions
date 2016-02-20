package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the event_participant database table.
 * 
 */
@Entity
@Table(name="event_participant")
@NamedQuery(name="EventParticipant.findAll", query="SELECT ep FROM EventParticipant ep")
public class EventParticipant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="event_participant_id")
	private int eventParticipantId;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="eventParticipant")
	private List<Comment> comments;

	//bi-directional many-to-one association to Event
	@ManyToOne
	@JoinColumn(name="event_id")
	private Event event;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public EventParticipant() {
	}

	public int getEventParticipantId() {
		return this.eventParticipantId;
	}

	public void setEventParticipantId(int eventParticipantId) {
		this.eventParticipantId = eventParticipantId;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setEventParticipant(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setEventParticipant(null);

		return comment;
	}

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}