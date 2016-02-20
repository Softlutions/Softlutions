package com.cenfotec.dondeEs.pojo;

 
import java.util.List;


/**
 * The persistent class for the place database table.
 * 
 */
public class PlacePOJO {
	
	private int placeId;

	private String latitude;

	private String longitude;

	private String name;

	private List<EventPOJO> events;

	public PlacePOJO() {
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

	public List<EventPOJO> getEvents() {
		return this.events;
	}

	public void setEvents(List<EventPOJO> events) {
		this.events = events;
	}


}