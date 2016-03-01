package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.EventPOJO;

public class EventResponse extends BaseResponse {
	private List<EventPOJO> eventList;

	public List<EventPOJO> getEventList() {
		return eventList;
	}

	public void setEventList(List<EventPOJO> eventList) {
		this.eventList = eventList;
	}
}