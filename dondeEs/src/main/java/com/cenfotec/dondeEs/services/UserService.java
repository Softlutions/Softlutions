package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.RolePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.pojo.UserTypePOJO;
import com.cenfotec.dondeEs.repositories.UserRepository;

@Service
public class UserService implements UserServiceInterface {
	@Autowired private UserRepository userRepository;
	
	public List<UserPOJO> getAll(){
		List<User> usersList = userRepository.findAll();
		List<UserPOJO> usersListPOJO = new ArrayList<>();
		
		usersList.stream().forEach(u -> {
			UserPOJO user = new UserPOJO();
			BeanUtils.copyProperties(u, user);
			
			if(u.getRole() != null){
				RolePOJO rolePOJO = new RolePOJO();
				BeanUtils.copyProperties(u.getRole(), rolePOJO);
				rolePOJO.setUsers(null);
				rolePOJO.setPermissions(null);
				user.setRole(rolePOJO);
			}
			
			if(u.getUserType() != null){
				UserTypePOJO userTypePOJO = new UserTypePOJO();
				BeanUtils.copyProperties(u.getUserType(), userTypePOJO);
				userTypePOJO.setUsers(null);
				user.setUserType(userTypePOJO);
			}
			
			usersListPOJO.add(user);
		});
		
		return usersListPOJO;
	}
}
