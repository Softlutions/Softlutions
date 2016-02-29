package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Role;
import com.cenfotec.dondeEs.pojo.RolePOJO;

public interface RoleServiceInterface {
	List<RolePOJO> getAll();
	
	Boolean saveRole(Role role);
	
	void deleteRole(Role role);

}
