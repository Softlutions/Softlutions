package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the event database table.
 * 
 */
@Entity
@Table(name="event")
@NamedQuery(name="Event.findAll", query="SELECT e FROM Event e")
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="event_id")
	private int eventId;

	private String description;

	private String image;

	private String name;

	@Column(name="private")
	private byte private_;

	@Temporal(TemporalType.DATE)
	private Date publishDate;

	@Temporal(TemporalType.DATE)
	private Date registerDate;

	private byte state;

	//bi-directional many-to-one association to Chat
	@OneToMany(mappedBy="event")
	private List<Chat> chats;

	//bi-directional many-to-many association to EventAttribute
	@ManyToMany
	@JoinTable(
		name="assigned_event_attribute"
		, joinColumns={
			@JoinColumn(name="event_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="event_attribute_id")
			}
		)
	private List<EventAttribute> eventAttributes;

	//bi-directional many-to-one association to EventType
	@ManyToOne
	@JoinColumn(name="event_type_id")
	private EventType eventType;

	//bi-directional many-to-one association to Place
	@ManyToOne
	@JoinColumn(name="place_id")
	private Place place;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	//bi-directional many-to-one association to EventParticipant
	@OneToMany(mappedBy="event")
	private List<EventParticipant> eventParticipants;

	//bi-directional many-to-one association to Note
	@OneToMany(mappedBy="event")
	private List<Note> notes;

	//bi-directional many-to-one association to ServiceContract
	@OneToMany(mappedBy="event")
	private List<ServiceContract> serviceContracts;

	public Event() {
	}

	public int getEventId() {
		return this.eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getPrivate_() {
		return this.private_;
	}

	public void setPrivate_(byte private_) {
		this.private_ = private_;
	}

	public Date getPublishDate() {
		return this.publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public byte getState() {
		return this.state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public List<Chat> getChats() {
		return this.chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public Chat addChat(Chat chat) {
		getChats().add(chat);
		chat.setEvent(this);

		return chat;
	}

	public Chat removeChat(Chat chat) {
		getChats().remove(chat);
		chat.setEvent(null);

		return chat;
	}

	public List<EventAttribute> getEventAttributes() {
		return this.eventAttributes;
	}

	public void setEventAttributes(List<EventAttribute> eventAttributes) {
		this.eventAttributes = eventAttributes;
	}

	public EventType getEventType() {
		return this.eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Place getPlace() {
		return this.place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<EventParticipant> getEventParticipants() {
		return this.eventParticipants;
	}

	public void setEventParticipants(List<EventParticipant> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public EventParticipant addEventParticipant(EventParticipant eventParticipant) {
		getEventParticipants().add(eventParticipant);
		eventParticipant.setEvent(this);

		return eventParticipant;
	}

	public EventParticipant removeEventParticipant(EventParticipant eventParticipant) {
		getEventParticipants().remove(eventParticipant);
		eventParticipant.setEvent(null);

		return eventParticipant;
	}

	public List<Note> getNotes() {
		return this.notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Note addNote(Note note) {
		getNotes().add(note);
		note.setEvent(this);

		return note;
	}

	public Note removeNote(Note note) {
		getNotes().remove(note);
		note.setEvent(null);

		return note;
	}

	public List<ServiceContract> getServiceContracts() {
		return this.serviceContracts;
	}

	public void setServiceContracts(List<ServiceContract> serviceContracts) {
		this.serviceContracts = serviceContracts;
	}

	public ServiceContract addServiceContract(ServiceContract serviceContract) {
		getServiceContracts().add(serviceContract);
		serviceContract.setEvent(this);

		return serviceContract;
	}

	public ServiceContract removeServiceContract(ServiceContract serviceContract) {
		getServiceContracts().remove(serviceContract);
		serviceContract.setEvent(null);

		return serviceContract;
	}

}