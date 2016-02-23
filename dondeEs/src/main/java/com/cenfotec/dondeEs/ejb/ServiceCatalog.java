package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the service_catalog database table.
 * 
 */
@Entity
@Table(name="service_catalog")
@NamedQuery(name="ServiceCatalog.findAll", query="SELECT s FROM ServiceCatalog s")
public class ServiceCatalog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="service_catalog_id")
	private int serviceCatalogId;

	private String name;

	//bi-directional many-to-one association to Service
	@OneToMany(mappedBy="serviceCatalog")
	private List<Service> services;

	public ServiceCatalog() {
	}

	public int getServiceCatalogId() {
		return this.serviceCatalogId;
	}

	public void setServiceCatalogId(int serviceCatalogId) {
		this.serviceCatalogId = serviceCatalogId;
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
		service.setServiceCatalog(this);

		return service;
	}

	public Service removeService(Service service) {
		getServices().remove(service);
		service.setServiceCatalog(null);

		return service;
	}

}