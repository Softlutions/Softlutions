package com.cenfotec.dondeEs.contracts;

import java.util.List;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;

public class UserResponse extends BaseResponse {
	private int userId;
	
	private List<UserPOJO> listUser;
	
	private UserPOJO user;

	private List<ServicePOJO> listService;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public List<ServicePOJO> getListService() {
		return listService;
	}

	public void setListService(List<ServicePOJO> list) {
		this.listService = list;
	}

	public List<UserPOJO> getListUser() {
		return listUser;
	}

	public void setListUser(List<UserPOJO> listUser) {
		this.listUser = listUser;
	}

	public UserPOJO getUser() {
		return user;
	}

	public void setUser(UserPOJO user) {
		this.user = user;
	}
}
