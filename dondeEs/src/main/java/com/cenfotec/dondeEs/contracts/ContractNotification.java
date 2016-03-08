package com.cenfotec.dondeEs.contracts;

import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;

public class ContractNotification extends BaseRequest {
	EventPOJO event;
	ServicePOJO service;
	
	public ContractNotification() {
		super();
	}

	public EventPOJO getEvent() {
		return event;
	}

	public void setEvent(EventPOJO event) {
		this.event = event;
	}

	public ServicePOJO getService() {
		return service;
	}

	public void setService(ServicePOJO service) {
		this.service = service;
	}
	
	
	
	
	
}
