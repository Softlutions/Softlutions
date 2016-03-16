package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.ChatPOJO;

public class ChatResponse extends BaseResponse {

	List<ChatPOJO> chats;

	public List<ChatPOJO> getChats() {
		return chats;
	}

	public void setChats(List<ChatPOJO> chats) {
		this.chats = chats;
	}
	
	
}
