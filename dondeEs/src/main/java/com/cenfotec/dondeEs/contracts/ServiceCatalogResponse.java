package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.ServiceCatalog;

public class ServiceCatalogResponse extends BaseResponse {

	List<ServiceCatalog> serviceCatalogList;

	public List<ServiceCatalog> getServiceCatalogList() {
		return serviceCatalogList;
	}

	public void setServiceCatalogList(List<ServiceCatalog> serviceCatalogList) {
		this.serviceCatalogList = serviceCatalogList;
	}
	
	
}
