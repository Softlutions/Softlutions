package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the service_contract database table.
 * 
 */
@Entity
@Table(name="service_contract")
@NamedQuery(name="ServiceContract.findAll", query="SELECT s FROM ServiceContract s")
public class ServiceContract implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_service")
	private int idService;

	private String comment;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private byte state;

	//bi-directional many-to-one association to Event
	@ManyToOne
	@JoinColumn(name="event_id")
	private Event event;

	//bi-directional many-to-one association to Service
	@ManyToOne
	@JoinColumn(name="service_id")
	private Service service;

	public ServiceContract() {
	}

	public int getIdService() {
		return this.idService;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
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