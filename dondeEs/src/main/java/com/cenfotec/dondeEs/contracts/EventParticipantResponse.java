package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;

public class EventParticipantResponse {
	
	public List<EventParticipantPOJO> eventParticipantsList;

	public List<EventParticipantPOJO> getEventParticipantsList() {
		return eventParticipantsList;
	}

	public void setEventParticipantsList(List<EventParticipantPOJO> peventParticipantsList) {
		this.eventParticipantsList = peventParticipantsList;
	}
}
