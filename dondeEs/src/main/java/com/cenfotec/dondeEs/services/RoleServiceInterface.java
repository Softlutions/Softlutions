package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Role;

public interface RoleServiceInterface {
	List<Role> getAll();
	
	Boolean saveRole(Role role);
	
	void deleteRole(Role role);

}
