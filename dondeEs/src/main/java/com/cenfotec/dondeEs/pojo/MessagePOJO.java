package com.cenfotec.dondeEs.pojo;


import java.util.Date;


/**
 * The persistent class for the message database table.
 * 
 */

public class MessagePOJO {

	private int messageId;

	private String content;

	private Date time;

	private ChatPOJO chat;

	private UserPOJO user;

	public MessagePOJO() {
	}

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public ChatPOJO getChat() {
		return this.chat;
	}

	public void setChat(ChatPOJO chat) {
		this.chat = chat;
	}

	public UserPOJO getUser() {
		return this.user;
	}

	public void setUser(UserPOJO user) {
		this.user = user;
	}

}