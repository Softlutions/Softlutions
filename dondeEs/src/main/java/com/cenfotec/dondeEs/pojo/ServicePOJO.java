package com.cenfotec.dondeEs.pojo;

 
import java.util.List;

import com.cenfotec.dondeEs.ejb.ServiceCatalog;


/**
 * The persistent class for the service database table.
 * 
 */
public class ServicePOJO {
	private int serviceId;

	private String description;

	private String name;

	private byte state;

	private UserPOJO user;

	private List<ServiceContactPOJO> serviceContacts;

	private ServiceCatalogPOJO serviceCatalog;

	public ServicePOJO() {
	}

	public int getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getState() {
		return this.state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public UserPOJO getUser() {
		return this.user;
	}

	public void setUser(UserPOJO user) {
		this.user = user;
	}

	public List<ServiceContactPOJO> getServiceContacts() {
		return this.serviceContacts;
	}

	public ServiceCatalogPOJO getServiceCatalog() {
		return serviceCatalog;
	}

	public void setServiceCatalog(ServiceCatalogPOJO serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	public void setServiceContacts(List<ServiceContactPOJO> serviceContacts) {
		this.serviceContacts = serviceContacts;
	}

	
}