package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;


/**
 * The persistent class for the place database table.
 * 
 */
@Entity
@Table(name="place")
@NamedQuery(name="Place.findAll", query="SELECT p FROM Place p")
public class Place implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="place_id")
	private int placeId;

	@Lob
	private String latitude;

	@Lob
	private String longitude;

	private String name;

	//bi-directional many-to-one association to Event
	@OneToMany(mappedBy="place")
	@JsonBackReference
	private List<Event> events;

	public Place() {
	}

	public int getPlaceId() {
		return this.placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Event> getEvents() {
		return this.events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Event addEvent(Event event) {
		getEvents().add(event);
		event.setPlace(this);

		return event;
	}

	public Event removeEvent(Event event) {
		getEvents().remove(event);
		event.setPlace(null);

		return event;
	} 

}