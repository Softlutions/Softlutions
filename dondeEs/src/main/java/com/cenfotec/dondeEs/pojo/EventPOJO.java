package com.cenfotec.dondeEs.pojo;

 
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the event database table.
 * 
 */

public class EventPOJO {
	private int eventId;

	private String description;

	private String image;

	private String largeDescription;

	private String name;

	private byte private_;

	private Date publishDate;

	private Date registerDate;

	private byte state;

	private List<ChatPOJO> chats;

	//bi-directional many-to-one association to Place
	private PlacePOJO place;

	//bi-directional many-to-one association to User
	private UserPOJO user;

	//bi-directional many-to-one association to EventParticipant
	private List<EventParticipantPOJO> eventParticipants;

	private List<NotePOJO> notes;

	//bi-directional many-to-one association to ServiceContact
	private List<ServiceContact> serviceContacts;

	public EventPOJO() {
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

	public String getLargeDescription() {
		return this.largeDescription;
	}

	public void setLargeDescription(String largeDescription) {
		this.largeDescription = largeDescription;
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

	public List<ChatPOJO> getChats() {
		return this.chats;
	}

	public void setChats(List<ChatPOJO> chats) {
		this.chats = chats;
	}

	public ChatPOJO addChat(ChatPOJO chat) {
		getChats().add(chat);
		chat.setEvent(this);

		return chat;
	}

	public ChatPOJO removeChat(ChatPOJO chat) {
		getChats().remove(chat);
		chat.setEvent(null);

		return chat;
	}

	public PlacePOJO getPlace() {
		return this.place;
	}

	public void setPlace(PlacePOJO place) {
		this.place = place;
	}

	public UserPOJO getUser() {
		return this.user;
	}

	public void setUser(UserPOJO user) {
		this.user = user;
	}

	public List<EventParticipantPOJO> getEventParticipants() {
		return this.eventParticipants;
	}

	public void setEventParticipants(List<EventParticipantPOJO> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public EventParticipantPOJO addEventParticipant(EventParticipantPOJO eventParticipant) {
		getEventParticipants().add(eventParticipant);
		eventParticipant.setEvent(this);

		return eventParticipant;
	}

	public EventParticipantPOJO removeEventParticipant(EventParticipantPOJO eventParticipant) {
		getEventParticipants().remove(eventParticipant);
		eventParticipant.setEvent(null);

		return eventParticipant;
	}

	public List<NotePOJO> getNotes() {
		return this.notes;
	}

	public void setNotes(List<NotePOJO> notes) {
		this.notes = notes;
	}

	public NotePOJO addNote(NotePOJO note) {
		getNotes().add(note);
		note.setEvent(this);

		return note;
	}

	public NotePOJO removeNote(NotePOJO note) {
		getNotes().remove(note);
		note.setEvent(null);

		return note;
	}

	public List<ServiceContact> getServiceContacts() {
		return this.serviceContacts;
	}

	public void setServiceContacts(List<ServiceContact> serviceContacts) {
		this.serviceContacts = serviceContacts;
	}

	public ServiceContact addServiceContact(ServiceContact serviceContact) {
		getServiceContacts().add(serviceContact);
		serviceContact.setEvent(this);

		return serviceContact;
	}

	public ServiceContact removeServiceContact(ServiceContact serviceContact) {
		getServiceContacts().remove(serviceContact);
		serviceContact.setEvent(null);

		return serviceContact;
	}

}