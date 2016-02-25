package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ServicePOJO;

public class UserResponse extends BaseResponse {
	private List<User> listUser;

	private List<ServicePOJO> listService;
	
	public List<ServicePOJO> getListService() {
		return listService;
	}

	public void setListService(List<ServicePOJO> list) {
		this.listService = list;
	}

	public List<User> getListUser() {
		return listUser;
	}

	public void setListUser(List<User> listUser) {
		this.listUser = listUser;
	}
}
