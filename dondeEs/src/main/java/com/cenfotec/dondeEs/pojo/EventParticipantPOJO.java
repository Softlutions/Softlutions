package com.cenfotec.dondeEs.pojo;

 
import java.util.Date;
import java.util.List;



/**
 * The persistent class for the event_participant database table.
 * 
 */

public class EventParticipantPOJO{
	private int eventParticipantId;

	private Date invitationDate;

	private byte state;

	private List<CommentPOJO> comments;

	private EventPOJO event;

	private UserPOJO user;
	
	private OfflineUserPOJO offlineUser;

	public EventParticipantPOJO() {
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

	public List<CommentPOJO> getComments() {
		return this.comments;
	}

	public void setComments(List<CommentPOJO> comments) {
		this.comments = comments;
	}

	public EventPOJO getEvent() {
		return this.event;
	}

	public void setEvent(EventPOJO event) {
		this.event = event;
	}

	public UserPOJO getUser() {
		return this.user;
	}

	public void setUser(UserPOJO user) {
		this.user = user;
	}

	public OfflineUserPOJO getOfflineUser() {
		return offlineUser;
	}

	public void setOfflineUser(OfflineUserPOJO offlineUser) {
		this.offlineUser = offlineUser;
	}

}