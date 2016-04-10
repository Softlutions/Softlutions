package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cenfotec.dondeEs.contracts.LoginRequest;
import com.cenfotec.dondeEs.contracts.LoginResponse;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.logic.AES;
import com.cenfotec.dondeEs.pojo.PermissionPOJO;
import com.cenfotec.dondeEs.pojo.RolePOJO;
import com.cenfotec.dondeEs.repositories.LoginRepository;

@Service
public class LoginService implements LoginServiceInterface{

	@Autowired private LoginRepository loginRepository;
	
	@Override
	@Transactional
	public void checkUser(LoginRequest lr, LoginResponse response, HttpSession currentSession) {
		String pass = lr.isCript()? lr.getPassword():AES.base64encode(lr.getPassword());
		User loggedUser = loginRepository.findByEmailAndPassword(lr.getEmail(), pass);
		
		if(loggedUser == null){
			response.setCode(401);
			response.setErrorMessage("Unauthorized user");
			response.setIdUser(-1);
			
			currentSession.setAttribute("idUser", -1);
		}else{
			response.setCode(200);
			response.setCodeMessage("User authorized");
			response.setIdUser(loggedUser.getUserId());
			response.setFirstName(loggedUser.getName());
			response.setLastName(loggedUser.getLastName1());
			response.setEmail(loggedUser.getEmail());
			response.setCriptPass(pass);
			
			/*Esta linea deberia de servir*/
			response.setState((byte)loggedUser.getState());
			/*Esta linea deberia de servir*/
			
			RolePOJO rolePOJO = new RolePOJO();
			BeanUtils.copyProperties(loggedUser.getRole(), rolePOJO);
			rolePOJO.setUsers(null);
			response.setRole(rolePOJO);
			
			List<PermissionPOJO> permissions = new ArrayList<>();
			loggedUser.getRole().getPermissions().stream().forEach(p -> {
				PermissionPOJO permission = new PermissionPOJO();
				BeanUtils.copyProperties(p, permission);
				permission.setRoles(null);
				permissions.add(permission);
			});
			
			rolePOJO.setPermissions(permissions);
			
			currentSession.setAttribute("idUser", loggedUser.getUserId());
		}
	}
}