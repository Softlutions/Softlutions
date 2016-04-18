package com.cenfotec.dondeEs.contracts;
import com.cenfotec.dondeEs.ejb.User;
public class UserRequest extends BaseRequest {
	
	private User user;
	
	public UserRequest() {
		super();
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "UsersRequest [user=" + user + "]";
	}
}