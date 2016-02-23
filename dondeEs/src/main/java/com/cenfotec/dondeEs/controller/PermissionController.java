package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.PermissionResponse;
import com.cenfotec.dondeEs.ejb.Permission;
import com.cenfotec.dondeEs.services.PermissionServiceInterface;



@RestController
@RequestMapping(value = "rest/protected/permission")
public class PermissionController {
	
	@Autowired private PermissionServiceInterface permissionServiceInterface;
	
//	get all
	@RequestMapping(value ="/getAll", method = RequestMethod.GET)
	public PermissionResponse getAll(){	
			
		PermissionResponse response = new PermissionResponse();
		response.setCode(200);
		response.setCodeMessage("users fetch success");
		response.setPermissionList(permissionServiceInterface.getAll());
		return response;
	}
	
	@RequestMapping(value ="/createPermission", method = RequestMethod.POST)
	public PermissionResponse createPermission(@RequestBody Permission permission){
		PermissionResponse pr = new PermissionResponse();
		
		Permission permission2 = new Permission();
		permission2.setName(permission.getName());
		
		Boolean state= permissionServiceInterface.savePermission(permission2);
		
		if(state){
			pr.setCode(200);
			pr.setCodeMessage("succesfull");
		}else{
			pr.setCode(400);
			pr.setCodeMessage("error");
		}
		return pr;
	}

	@RequestMapping(value ="/modifyPermission", method = RequestMethod.PUT)
	public PermissionResponse modifyPermission(@RequestBody Permission permission){
		PermissionResponse pr = new PermissionResponse();
		
		Permission npermission = new Permission();
		npermission.setPermissionId(permission.getPermissionId());
		npermission.setName(permission.getName());
		
		
		Boolean state= permissionServiceInterface.savePermission(npermission);
		
		if(state){
			pr.setCode(200);
			pr.setCodeMessage("succesfull");
		}else{
			pr.setCode(400);
			pr.setCodeMessage("error");
		}
		return pr;
	}
	
	@RequestMapping(value ="/deleteRents", method = RequestMethod.DELETE)
	public PermissionResponse deleteRents(@RequestBody Permission alquiler){
		PermissionResponse rs = new PermissionResponse();
		
		permissionServiceInterface.delete(alquiler);
		
			rs.setCode(200);
			rs.setCodeMessage("type user update succesfully");
		
		return rs;		
	}

}
