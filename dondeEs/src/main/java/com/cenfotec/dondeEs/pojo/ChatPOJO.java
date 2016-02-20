package com.cenfotec.dondeEs.pojo;


import java.util.List;


/**
 * The persistent class for the chat database table.
 * 
 */

public class ChatPOJO   {
	
	private int chatId;

	private EventPOJO event;

	private List<MessagePOJO> messages;

	private List<UserPOJO> users;

	public ChatPOJO() {
	}

	public int getChatId() {
		return this.chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public EventPOJO getEvent() {
		return this.event;
	}

	public void setEvent(EventPOJO event) {
		this.event = event;
	}

	public List<MessagePOJO> getMessages() {
		return this.messages;
	}

	public void setMessages(List<MessagePOJO> messages) {
		this.messages = messages;
	}

	public List<UserPOJO> getUsers() {
		return this.users;
	}

	public void setUsers(List<UserPOJO> users) {
		this.users = users;
	}

}