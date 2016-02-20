package com.cenfotec.dondeEs.pojo;
 
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
public class UserPOJO  {

	private int userId;

	private String email;

	private String image;

	private String lastName1;

	private String lastName2;

	private String name;

	private String password;

	private String phone;

	private byte state;

	private List<EventPOJO> events;

	private List<EventParticipantPOJO> eventParticipants;

	private List<MessagePOJO> messages;

	private List<PasswordHistoryPOJO> passwordHistories;

	private List<ServicePOJO> services;

	private List<ChatPOJO> chats;

	private RolePOJO role;

	private List<UserPOJO> users1;

	private List<UserPOJO> users2;

	public UserPOJO() {
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLastName1() {
		return this.lastName1;
	}

	public void setLastName1(String lastName1) {
		this.lastName1 = lastName1;
	}

	public String getLastName2() {
		return this.lastName2;
	}

	public void setLastName2(String lastName2) {
		this.lastName2 = lastName2;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public byte getState() {
		return this.state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public List<EventPOJO> getEvents() {
		return this.events;
	}

	public void setEvents(List<EventPOJO> events) {
		this.events = events;
	}



	public List<EventParticipantPOJO> getEventParticipants() {
		return this.eventParticipants;
	}

	public void setEventParticipants(List<EventParticipantPOJO> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public List<MessagePOJO> getMessages() {
		return this.messages;
	}

	public void setMessages(List<MessagePOJO> messages) {
		this.messages = messages;
	}

	public List<PasswordHistoryPOJO> getPasswordHistories() {
		return this.passwordHistories;
	}

	public void setPasswordHistories(List<PasswordHistoryPOJO> passwordHistories) {
		this.passwordHistories = passwordHistories;
	}


	public List<ServicePOJO> getServices() {
		return this.services;
	}

	public void setServices(List<ServicePOJO> services) {
		this.services = services;
	}


	public List<ChatPOJO> getChats() {
		return this.chats;
	}

	public void setChats(List<ChatPOJO> chats) {
		this.chats = chats;
	}

	public RolePOJO getRole() {
		return this.role;
	}

	public void setRole(RolePOJO role) {
		this.role = role;
	}

	public List<UserPOJO> getUsers1() {
		return this.users1;
	}

	public void setUsers1(List<UserPOJO> users1) {
		this.users1 = users1;
	}

	public List<UserPOJO> getUsers2() {
		return this.users2;
	}

	public void setUsers2(List<UserPOJO> users2) {
		this.users2 = users2;
	}

}