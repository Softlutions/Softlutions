package com.cenfotec.dondeEs.contracts;

public class ServiceContactRequest {

	String eventId;
	String serviceId;
	byte state;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;	
	}
}
