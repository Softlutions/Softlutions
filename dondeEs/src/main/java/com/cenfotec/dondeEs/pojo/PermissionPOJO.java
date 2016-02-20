package com.cenfotec.dondeEs.pojo;

 
import java.util.List;


/**
 * The persistent class for the permission database table.
 * 
 */
public class PermissionPOJO {

	private int permissionId;

	private String name;

	private List<RolePOJO> roles;

	public PermissionPOJO() {
	}

	public int getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RolePOJO> getRoles() {
		return this.roles;
	}

	public void setRoles(List<RolePOJO> roles) {
		this.roles = roles;
	}

}