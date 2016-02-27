package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.UserPOJO;

public class UserResponse extends BaseResponse {
	private List<UserPOJO> listUser;

	public List<UserPOJO> getListUser() {
		return listUser;
	}

	public void setListUser(List<UserPOJO> listUser) {
		this.listUser = listUser;
	}
}
