package com.cenfotec.dondeEs.pojo;


import java.util.List;




public class ServiceCatalogPOJO {
	private int serviceCatalogId;

	private String name;

	private List<AuctionPOJO> auctions;
//	private List<ServicePOJO> services;

	public ServiceCatalogPOJO() {
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

	public List<AuctionPOJO> getAuctions() {
		return auctions;
	}

	public void setAuctions(List<AuctionPOJO> auctions) {
		this.auctions = auctions;
	}

//	public List<ServicePOJO> getServices() {
//		return this.services;
//	}
//
//	public void setServices(List<ServicePOJO> services) {
//		this.services = services;
//	}
//
//	public ServicePOJO addService(ServicePOJO service) {
//		getServices().add(service);
//		service.setServiceCatalog(this);
//
//		return service;
//	}
//
//	public ServicePOJO removeService(ServicePOJO service) {
//		getServices().remove(service);
//		service.setServiceCatalog(null);
//
//		return service;
//	}

}