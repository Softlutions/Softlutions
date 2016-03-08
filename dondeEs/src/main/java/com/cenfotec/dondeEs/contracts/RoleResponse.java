package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.RolePOJO;

public class RoleResponse extends BaseResponse {
	
	private List<RolePOJO> listRole;

	public List<RolePOJO> getListRole() {
		return listRole;
	}

	public void setListRole(List<RolePOJO> listRole) {
		this.listRole = listRole;
	}
}
