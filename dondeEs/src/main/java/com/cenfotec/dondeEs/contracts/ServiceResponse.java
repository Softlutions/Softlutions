package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.pojo.ServicePOJO;

public class ServiceResponse extends BaseResponse {
	private List<Service> serviceList;
	private List<ServicePOJO> serviceLists;
	private ServicePOJO service;

	public List<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}

	public List<ServicePOJO> getServiceLists() {
		return serviceLists;
	}

	public void setServiceLists(List<ServicePOJO> serviceLists) {
		this.serviceLists = serviceLists;
	}

	public ServicePOJO getService() {
		return service;
	}

	public void setService(ServicePOJO service) {
		this.service = service;
	}
	
	
	
	
}
