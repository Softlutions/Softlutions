package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the event_attribute database table.
 * 
 */
@Entity
@Table(name="event_attribute")
@NamedQuery(name="EventAttribute.findAll", query="SELECT e FROM EventAttribute e")
public class EventAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="event_attribute_id")
	private int eventAttributeId;

	private String name;

	//bi-directional many-to-many association to Event
	@ManyToMany(mappedBy="eventAttributes")
	private List<Event> events;

	//bi-directional many-to-one association to DataType
	@ManyToOne
	@JoinColumn(name="data_type_id")
	private DataType dataType;

	//bi-directional many-to-one association to EventType
	@ManyToOne
	@JoinColumn(name="event_type_id")
	private EventType eventType;

	public EventAttribute() {
	}

	public int getEventAttributeId() {
		return this.eventAttributeId;
	}

	public void setEventAttributeId(int eventAttributeId) {
		this.eventAttributeId = eventAttributeId;
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

	public DataType getDataType() {
		return this.dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public EventType getEventType() {
		return this.eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

}