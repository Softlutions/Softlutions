package com.cenfotec.dondeEs.pojo;

 


/**
 * The persistent class for the service_contact database table.
 * 
 */
public class ServiceContactPOJO {

	private int serviceContractId;

	private String comment;

	private byte state;

	private EventPOJO event;

	//bi-directional many-to-one association to Service
	private ServicePOJO service;

	public ServiceContactPOJO() {
	}

	public int getServiceContractId() {
		return this.serviceContractId;
	}

	public void setServiceContractId(int serviceContractId) {
		this.serviceContractId = serviceContractId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public byte getState() {
		return this.state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public EventPOJO getEvent() {
		return this.event;
	}

	public void setEvent(EventPOJO event) {
		this.event = event;
	}

	public ServicePOJO getService() {
		return this.service;
	}

	public void setService(ServicePOJO service) {
		this.service = service;
	}

}