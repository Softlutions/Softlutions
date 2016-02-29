package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.UserPOJO;

public interface UserServiceInterface {
	public Boolean saveUser(UserRequest	 ur);
	List<UserPOJO> getAll();

}
