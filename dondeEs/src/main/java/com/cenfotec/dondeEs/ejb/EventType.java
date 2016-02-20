package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the event_type database table.
 * 
 */
@Entity
@Table(name="event_type")
@NamedQuery(name="EventType.findAll", query="SELECT e FROM EventType e")
public class EventType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="event_type_id")
	private int eventTypeId;

	private String type;

	//bi-directional many-to-one association to Event
	@OneToMany(mappedBy="eventType")
	private List<Event> events;

	//bi-directional many-to-one association to EventAttribute
	@OneToMany(mappedBy="eventType")
	private List<EventAttribute> eventAttributes;

	public EventType() {
	}

	public int getEventTypeId() {
		return this.eventTypeId;
	}

	public void setEventTypeId(int eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Event> getEvents() {
		return this.events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Event addEvent(Event event) {
		getEvents().add(event);
		event.setEventType(this);

		return event;
	}

	public Event removeEvent(Event event) {
		getEvents().remove(event);
		event.setEventType(null);

		return event;
	}

	public List<EventAttribute> getEventAttributes() {
		return this.eventAttributes;
	}

	public void setEventAttributes(List<EventAttribute> eventAttributes) {
		this.eventAttributes = eventAttributes;
	}

	public EventAttribute addEventAttribute(EventAttribute eventAttribute) {
		getEventAttributes().add(eventAttribute);
		eventAttribute.setEventType(this);

		return eventAttribute;
	}

	public EventAttribute removeEventAttribute(EventAttribute eventAttribute) {
		getEventAttributes().remove(eventAttribute);
		eventAttribute.setEventType(null);

		return eventAttribute;
	}

}