package com.cenfotec.dondeEs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Role;
import com.cenfotec.dondeEs.repositories.RoleRepository;

@Service
public class RoleService implements RoleServiceInterface {

	@Autowired private RoleRepository roleRepository;
	
	@Override
	public List<Role> getAll(){
		List<Role> listRole = roleRepository.findAll();		
		return listRole;
	}
	
	@Override
	public Boolean saveRole(Role role){
	 	Role nrole = roleRepository.save(role);
	 	return (nrole == null) ? false : true;
	}
	
	@Override
	public void deleteRole(Role role){
		roleRepository.delete(role);
	}
}
