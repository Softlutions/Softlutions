package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the service_type database table.
 * 
 */
@Entity
@Table(name="service_type")
@NamedQuery(name="ServiceType.findAll", query="SELECT s FROM ServiceType s")
public class ServiceType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="service_type_id")
	private int serviceTypeId;

	private String name;

	//bi-directional many-to-one association to Service
	@OneToMany(mappedBy="serviceType")
	private List<Service> services;

	public ServiceType() {
	}

	public int getServiceTypeId() {
		return this.serviceTypeId;
	}

	public void setServiceTypeId(int serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Service> getServices() {
		return this.services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public Service addService(Service service) {
		getServices().add(service);
		service.setServiceType(this);

		return service;
	}

	public Service removeService(Service service) {
		getServices().remove(service);
		service.setServiceType(null);

		return service;
	}

}