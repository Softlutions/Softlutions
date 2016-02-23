package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Permission;

public interface PermissionServiceInterface {
	
	List<Permission> getAll();
	
	Boolean savePermission(Permission permission);
	
	void delete(Permission permission);

}
