package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.contracts.UserResponse;
import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ServicePOJO;


public interface ServiceInterface {
	
	Boolean saveService(Service service);
	
	List<Service> getAll();
	
	List<Service> getAll(int user);

}
