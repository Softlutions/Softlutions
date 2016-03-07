package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;

public class UserResponse extends BaseResponse {
	private List<UserPOJO> listUser;

	private List<ServicePOJO> listService;
	
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
}
