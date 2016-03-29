package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.cenfotec.dondeEs.ejb.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cenfotec.dondeEs.pojo.ServiceCatalogPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.repositories.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceImplementation implements ServiceInterface {
	@Autowired
	private ServiceRepository serviceRepository;

	@Override
	public Boolean saveService(Service pservice) {
		Service nservice = serviceRepository.save(pservice);
		return (nservice == null) ? false : true;
	}
	 
	@Override
	public List<ServicePOJO> getAll() {
		List<com.cenfotec.dondeEs.ejb.Service> listService = serviceRepository.findAll();
		List<ServicePOJO> servicePOJOList = new ArrayList<>();
		listService.forEach(s -> {
			ServicePOJO service = new ServicePOJO();
			service.setServiceId(s.getServiceId());
			service.setName(s.getName());
			service.setDescription(s.getDescription());

			UserPOJO userPOJO = new UserPOJO();
			userPOJO.setUserId(s.getUser().getUserId());
			userPOJO.setName(s.getUser().getName());
			userPOJO.setLastName1(s.getUser().getLastName1());
			userPOJO.setLastName2(s.getUser().getLastName2());
			userPOJO.setEmail(s.getUser().getEmail());
			service.setUser(userPOJO);
			servicePOJOList.add(service);
		});

		return servicePOJOList;
	}

	@Override
	@Transactional
	public ServicePOJO getService(int idEvent){
		Service nservice = serviceRepository.findOne(idEvent);
		ServicePOJO servicePOJO = new ServicePOJO();
		servicePOJO.setDescription(nservice.getDescription());
		servicePOJO.setName(nservice.getName());
		servicePOJO.setServiceId(nservice.getServiceId());
		servicePOJO.setState(nservice.getState());
		return servicePOJO;
	}

	@Override
	@Transactional
	public List<ServicePOJO> getByCatalog(int catalogId) {
		List<Service> serviceList = serviceRepository.getByCatalogId(catalogId);
		List<ServicePOJO> servicePOJOList = new ArrayList<>();

		serviceList.forEach(s -> {
			ServicePOJO service = new ServicePOJO();
			service.setServiceId(s.getServiceId());
			service.setName(s.getName());
			service.setDescription(s.getDescription());

			UserPOJO userPOJO = new UserPOJO();
			userPOJO.setUserId(s.getUser().getUserId());
			userPOJO.setName(s.getUser().getName());
			userPOJO.setLastName1(s.getUser().getLastName1());
			userPOJO.setLastName2(s.getUser().getLastName2());
			userPOJO.setEmail(s.getUser().getEmail());
			service.setUser(userPOJO);

			servicePOJOList.add(service);
		});

		return servicePOJOList;
	}

	@Transactional
	public ServicePOJO getServiceById(int idService) {
		com.cenfotec.dondeEs.ejb.Service nservice = serviceRepository.findOne(idService);
		ServicePOJO servicePOJO = new ServicePOJO();
		BeanUtils.copyProperties(nservice, servicePOJO);
		UserPOJO userPOJO = new UserPOJO();
		BeanUtils.copyProperties(nservice.getUser(), userPOJO);
		servicePOJO.setUser(userPOJO);
		return servicePOJO;
	}

	@Override
	@Transactional
	public List<ServicePOJO> getByProvider(int idUser) {
		List<Service> service = serviceRepository.getService(idUser);
		List<ServicePOJO> servicePOJOList = new ArrayList<ServicePOJO>();
		service.stream().forEach(ta -> {
			ServicePOJO servicePOJO = new ServicePOJO();
			BeanUtils.copyProperties(ta, servicePOJO);
			servicePOJO.setServiceContacts(null);
			servicePOJOList.add(servicePOJO);
		});

		return servicePOJOList;
	}

	@Override
	public List<ServicePOJO> getAllServiceByUserAndServiceCatalog(int userId, int serviceCatalogId) {
		List<Service> serviceList = serviceRepository.findAllByUserUserIdAndServiceCatalogServiceCatalogId(userId,serviceCatalogId);
		List<ServicePOJO> servicePOJOList = new ArrayList<ServicePOJO>();
		serviceList.stream().forEach(s -> {
			ServicePOJO servicePOJO = new ServicePOJO();
			servicePOJO.setServiceId(s.getServiceId());
			servicePOJO.setName(s.getName());
			servicePOJO.setDescription(s.getDescription());
			servicePOJO.setState(s.getState());
			
			servicePOJOList.add(servicePOJO);
		});
		return servicePOJOList;
	}

	@Override
	public List<ServicePOJO> getServiceCatalogIdByProvider(int userId) {
		List<Service> serviceList = serviceRepository.findAllByUserUserId(userId);
		List<ServicePOJO> servicePOJOList = new ArrayList<ServicePOJO>();
		serviceList.stream().forEach(s -> {
			ServicePOJO servicePOJO = new ServicePOJO();
			ServiceCatalogPOJO serviceCatalogPOJO = new ServiceCatalogPOJO();
			serviceCatalogPOJO.setServiceCatalogId(s.getServiceCatalog().getServiceCatalogId());
			servicePOJO.setServiceCatalog(serviceCatalogPOJO);
			servicePOJOList.add(servicePOJO);
		});
		return servicePOJOList;
	}
	
	@Override
	public List<ServicePOJO> getServiceByServiceCatalog(int id) {
		List<Service> serviceList = serviceRepository.findAllByServiceCatalogServiceCatalogId(id);
		List<ServicePOJO> listPojo = new ArrayList<ServicePOJO>();
		serviceList.stream().forEach(ta -> {
			ServicePOJO servicePOJO = new ServicePOJO();
			BeanUtils.copyProperties(ta, servicePOJO);

			if (ta.getServiceCatalog() != null) {
				ServiceCatalogPOJO catalogPOJO = new ServiceCatalogPOJO();
				BeanUtils.copyProperties(ta.getServiceCatalog(), catalogPOJO);

				catalogPOJO.setAuctions(null);
				servicePOJO.setServiceCatalog(catalogPOJO);
			}
			servicePOJO.setServiceContacts(null);

			listPojo.add(servicePOJO);
		});
		return listPojo;
	}

}
