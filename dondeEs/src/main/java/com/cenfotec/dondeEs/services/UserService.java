package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.pojo.ServiceCatalogPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.repositories.UserRepository;

@org.springframework.stereotype.Service
public class UserService implements UserServiceInterface {
	@Autowired private UserRepository userRepository;
	
	@Override
	public List<User> getAll(){
		List<User> user = userRepository.findAll();
		return user;
	}
	
	@Override
	@Transactional
	public List<ServicePOJO> getAllService(int idUser){
		List<com.cenfotec.dondeEs.ejb.Service> listService = userRepository.getServicesByUser(idUser);
		List<ServicePOJO> listPojo = new ArrayList<ServicePOJO>();
		listService.stream().forEach(ta-> {
			ServicePOJO servicePOJO = new ServicePOJO();
			BeanUtils.copyProperties(ta, servicePOJO);
//			if(ta.getUser()!=null){
//				UserPOJO userPOJO = new UserPOJO();
//				BeanUtils.copyProperties(ta.getUser(), userPOJO);
//				servicePOJO.setUser(userPOJO);
//			}
			if(ta.getServiceCatalog() != null){
				ServiceCatalogPOJO catalogPOJO = new ServiceCatalogPOJO();
				BeanUtils.copyProperties(ta.getServiceCatalog(), catalogPOJO);
				servicePOJO.setServiceCatalog(catalogPOJO);
			}
				listPojo.add(servicePOJO);
				
				
			});
		
		
		return listPojo;
	}
	
}
