package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the service_contact database table.
 * 
 */
@Entity
@Table(name="service_contact")
@NamedQuery(name="ServiceContact.findAll", query="SELECT s FROM ServiceContact s")
public class ServiceContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="service_contract_id")
	private int serviceContractId;

	private String comment;

	private byte state;

	//bi-directional many-to-one association to Event
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="event_id")
	private Event event;

	//bi-directional many-to-one association to Service
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_id")
	private Service service;

	public ServiceContact() {
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

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Service getService() {
		return this.service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}