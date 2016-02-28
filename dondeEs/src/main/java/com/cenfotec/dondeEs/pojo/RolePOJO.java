package com.cenfotec.dondeEs.pojo;

 
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the role database table.
 * 
 */

public class RolePOJO {

	private int roleId;

	private String name;

	private byte state;

	private List<PermissionPOJO> permissions;

	private List<UserPOJO> users;

	public RolePOJO() {
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getState() {
		return this.state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public List<PermissionPOJO> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(List<PermissionPOJO> permissions) {
		this.permissions = permissions;
	}

	public List<UserPOJO> getUsers() {
		return this.users;
	}

	public void setUsers(List<UserPOJO> users) {
		this.users = users;
	}

}