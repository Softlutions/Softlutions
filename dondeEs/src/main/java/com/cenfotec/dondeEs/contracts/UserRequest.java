package com.cenfotec.dondeEs.contracts;
import com.cenfotec.dondeEs.pojo.UserPOJO;
public class UserRequest extends BaseRequest {
	
	private UserPOJO user;
	
	public UserRequest() {
		super();
	}
	
	public UserPOJO getUser() {
		return user;
	}
	
	public void setUser(UserPOJO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UsersRequest [user=" + user + "]";
	}
}