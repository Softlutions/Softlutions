package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the event_participant database table.
 * 
 */
@Entity
@Table(name="event_participant")
@NamedQuery(name="EventParticipant.findAll", query="SELECT e FROM EventParticipant e")
public class EventParticipant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="event_participant_id")
	private int eventParticipantId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="invitation_date")
	private Date invitationDate;

	private byte state;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="eventParticipant")
	private List<Comment> comments;

	//bi-directional many-to-one association to Event
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="event_id")
	private Event event;

	//bi-directional many-to-one association to OfflineUser
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="offline_user_id")
	private OfflineUser offlineUser;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

	//bi-directional many-to-one association to EventImage
	@OneToMany(mappedBy="eventParticipant")
	private List<EventImage> eventImages;

	public EventParticipant() {
	}

	public int getEventParticipantId() {
		return this.eventParticipantId;
	}

	public void setEventParticipantId(int eventParticipantId) {
		this.eventParticipantId = eventParticipantId;
	}

	public Date getInvitationDate() {
		return this.invitationDate;
	}

	public void setInvitationDate(Date invitationDate) {
		this.invitationDate = invitationDate;
	}

	public byte getState() {
		return this.state;
	}

	public void setState(byte state) {
		this.state = state;
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

	public OfflineUser getOfflineUser() {
		return this.offlineUser;
	}

	public void setOfflineUser(OfflineUser offlineUser) {
		this.offlineUser = offlineUser;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<EventImage> getEventImages() {
		return this.eventImages;
	}

	public void setEventImages(List<EventImage> eventImages) {
		this.eventImages = eventImages;
	}

	public EventImage addEventImage(EventImage eventImage) {
		getEventImages().add(eventImage);
		eventImage.setEventParticipant(this);

		return eventImage;
	}

	public EventImage removeEventImage(EventImage eventImage) {
		getEventImages().remove(eventImage);
		eventImage.setEventParticipant(null);

		return eventImage;
	}

}