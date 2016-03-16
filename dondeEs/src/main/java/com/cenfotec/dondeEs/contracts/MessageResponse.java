package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.MessagePOJO;

public class MessageResponse extends BaseResponse {

	 List<MessagePOJO> messages;

	public List<MessagePOJO> getMessages() {
		return messages;
	}

	public void setMessages(List<MessagePOJO> messages) {
		this.messages = messages;
	}
	
	 
}
