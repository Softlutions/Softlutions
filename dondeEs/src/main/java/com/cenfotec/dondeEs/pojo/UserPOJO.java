package com.cenfotec.dondeEs.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.cenfotec.dondeEs.ejb.Chat;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.ejb.Message;
import com.cenfotec.dondeEs.ejb.PasswordHistory;
import com.cenfotec.dondeEs.ejb.Role;
import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.ejb.TermCondition;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.ejb.UserType;

/**
 * The persistent class for the user database table.
 * 
 */
public class UserPOJO {

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

	// private List<ServicePOJO> services;

	private List<TermConditionPOJO> termConditions;

	private List<ChatPOJO> chats;

	private RolePOJO role;

	private List<UserPOJO> users1;

	private List<UserPOJO> users2;

	private UserTypePOJO userType;

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

	public void setState(boolean state) {
		this.state = (byte) (state ? 1 : 0);
	}

	public List<EventPOJO> getEvents() {
		return this.events;
	}

	public void setEvents(List<EventPOJO> events) {
		this.events = events;
	}

	public EventPOJO addEvent(EventPOJO event) {
		getEvents().add(event);
		event.setUser(this);

		return event;
	}

	public EventPOJO removeEvent(EventPOJO event) {
		getEvents().remove(event);
		event.setUser(null);

		return event;
	}

	public List<EventParticipantPOJO> getEventParticipants() {
		return this.eventParticipants;
	}

	public void setEventParticipants(List<EventParticipantPOJO> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public EventParticipantPOJO addEventParticipant(EventParticipantPOJO eventParticipant) {
		getEventParticipants().add(eventParticipant);
		eventParticipant.setUser(this);

		return eventParticipant;
	}

	public EventParticipant removeEventParticipant(EventParticipant eventParticipant) {
		getEventParticipants().remove(eventParticipant);
		eventParticipant.setUser(null);

		return eventParticipant;
	}

	public List<MessagePOJO> getMessages() {
		return this.messages;
	}

	public void setMessages(List<MessagePOJO> messages) {
		this.messages = messages;
	}

	public MessagePOJO addMessage(MessagePOJO message) {
		getMessages().add(message);
		message.setUser(this);

		return message;
	}

	public MessagePOJO removeMessage(MessagePOJO message) {
		getMessages().remove(message);
		message.setUser(null);

		return message;
	}

	public List<PasswordHistoryPOJO> getPasswordHistories() {
		return this.passwordHistories;
	}

	public void setPasswordHistories(List<PasswordHistoryPOJO> passwordHistories) {
		this.passwordHistories = passwordHistories;
	}

	public PasswordHistoryPOJO addPasswordHistory(PasswordHistoryPOJO passwordHistory) {
		getPasswordHistories().add(passwordHistory);
		passwordHistory.setUser(this);

		return passwordHistory;
	}

	public PasswordHistoryPOJO removePasswordHistory(PasswordHistoryPOJO passwordHistory) {
		getPasswordHistories().remove(passwordHistory);
		passwordHistory.setUser(null);

		return passwordHistory;
	}

	// public List<ServicePOJO> getServices() {
	// return this.services;
	// }
	//
	// public void setServices(List<ServicePOJO> services) {
	// this.services = services;
	// }

	// public ServicePOJO addService(ServicePOJO service) {
	// getServices().add(service);
	// service.setUser(this);
	//
	// return service;
	// }
	//
	// public ServicePOJO removeService(ServicePOJO service) {
	// getServices().remove(service);
	// service.setUser(null);
	//
	// return service;
	// }

	public List<TermConditionPOJO> getTermConditions() {
		return this.termConditions;
	}

	public void setTermConditions(List<TermConditionPOJO> termConditions) {
		this.termConditions = termConditions;
	}

	public TermConditionPOJO addTermCondition(TermConditionPOJO termCondition) {
		getTermConditions().add(termCondition);
		termCondition.setUser(this);

		return termCondition;
	}

	public TermConditionPOJO removeTermCondition(TermConditionPOJO termCondition) {
		getTermConditions().remove(termCondition);
		termCondition.setUser(null);

		return termCondition;
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

	public UserTypePOJO getUserType() {
		return this.userType;
	}

	public void setUserType(UserTypePOJO userType) {
		this.userType = userType;
	}

}