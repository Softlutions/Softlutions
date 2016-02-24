package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.EventPublish;

public class EventPublishResponse extends BaseResponse {
	private List<EventPublish> eventPublishList;

	public List<EventPublish> getEventPublishList() {
		return eventPublishList;
	}

	public void setEventPublishList(List<EventPublish> eventPublishList) {
		this.eventPublishList = eventPublishList;
	}
}
