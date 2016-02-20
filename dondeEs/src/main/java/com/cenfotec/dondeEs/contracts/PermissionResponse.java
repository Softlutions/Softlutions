package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Permission;

public class PermissionResponse extends BaseResponse {
	private List<Permission> permissionList;

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
}
