package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.EventParticipant;

public class EventParticipantResponse extends BaseResponse {
	List<EventParticipant> listEventParticipant;
	
	EventParticipant eventParticipant;
	
	public List<EventParticipant> getListEventParticipant() {
		return listEventParticipant;
	}

	public void setListEventParticipant(List<EventParticipant> listEventParticipant) {
		this.listEventParticipant = listEventParticipant;
	}

	public EventParticipant getEventParticipant() {
		return eventParticipant;
	}

	public void setEventParticipant(EventParticipant eventParticipant) {
		this.eventParticipant = eventParticipant;
	}
	
	
}
