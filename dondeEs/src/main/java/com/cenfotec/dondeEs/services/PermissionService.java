package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cenfotec.dondeEs.ejb.Permission;
import com.cenfotec.dondeEs.repositories.PermissionRepository;


@Service
public class PermissionService implements PermissionServiceInterface {
	
	@Autowired private PermissionRepository permissionRepository;
	
	@Override
	public List<Permission> getAll(){
		List<Permission> listPermission = permissionRepository.findAll();
		return listPermission;
	}

	@Override
	public Boolean saveTypeUser(Permission ptipoUsuario) {
		Permission tipoUsuario= permissionRepository.save(ptipoUsuario);
		return (tipoUsuario == null) ? false : true;
	}
	
	@Override
	public void delete(Permission permission){
		permissionRepository.delete(permission);;
	}
}
