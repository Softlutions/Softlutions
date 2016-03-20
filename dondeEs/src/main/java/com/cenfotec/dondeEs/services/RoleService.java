package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Role;
import com.cenfotec.dondeEs.pojo.RolePOJO;
import com.cenfotec.dondeEs.repositories.RoleRepository;

@Service
public class RoleService implements RoleServiceInterface {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<RolePOJO> getAll() {
		List<RolePOJO> listRolePOJOS = new ArrayList<>();
		roleRepository.findByNameNotLike("Administrador").stream().forEach(r -> {
			RolePOJO rolePOJO = new RolePOJO();
			rolePOJO.setUsers(null);
			rolePOJO.setPermissions(null);
			rolePOJO.setName(r.getName());
			rolePOJO.setRoleId(r.getRoleId());
			listRolePOJOS.add(rolePOJO);
		});
		return listRolePOJOS;
	}

	@Override
	public Boolean saveRole(Role role) {
		Role nrole = roleRepository.save(role);

		return (nrole == null) ? false : true;
	}

	@Override
	public void deleteRole(Role role) {
		roleRepository.delete(role);	
	}
}
