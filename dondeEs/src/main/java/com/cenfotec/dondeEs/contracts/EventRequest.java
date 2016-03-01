package com.cenfotec.dondeEs.contracts;

import com.cenfotec.dondeEs.pojo.EventPOJO;

public class EventRequest extends BaseRequest {
	private EventPOJO event;

	public EventPOJO getEvent() {
		return event;
	}

	public void setEvent(EventPOJO event) {
		this.event = event;
	}
	
	@Override
	public String toString() {
		return "EventsRequest [event=" + event + "]";
	}
}
