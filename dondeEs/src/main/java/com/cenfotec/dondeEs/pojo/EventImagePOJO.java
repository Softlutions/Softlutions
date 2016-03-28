package com.cenfotec.dondeEs.pojo;

public class EventImagePOJO{

	private int eventImageId;

	private EventParticipantPOJO eventParticipant;
	
	private String image;

	public EventImagePOJO() {
	}

	public int getEventImageId() {
		return this.eventImageId;
	}

	public void setEventImageId(int eventImageId) {
		this.eventImageId = eventImageId;
	}

	public EventParticipantPOJO getEventParticipant() {
		return eventParticipant;
	}

	public void setEventParticipant(EventParticipantPOJO eventParticipant) {
		this.eventParticipant = eventParticipant;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}