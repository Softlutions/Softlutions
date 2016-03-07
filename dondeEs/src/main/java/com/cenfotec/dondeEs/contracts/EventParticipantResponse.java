package com.cenfotec.dondeEs.contracts;

import java.util.List;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.ejb.EventParticipant;

public class EventParticipantResponse extends BaseResponse{
	
	public List<EventParticipantPOJO> eventParticipantsList;

	List<EventParticipant> listEventParticipant;
	
	EventParticipant eventParticipant;

	public List<EventParticipantPOJO> getEventParticipantsList() {
		return eventParticipantsList;
	}

	public void setEventParticipantsList(List<EventParticipantPOJO> peventParticipantsList) {
		this.eventParticipantsList = peventParticipantsList;
	}
	
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