package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Event;

public class EventResponse extends BaseResponse {
	private List<Event> eventList;
	
	public List<Event> getEventList(){
		return eventList;
	}
	
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
}
