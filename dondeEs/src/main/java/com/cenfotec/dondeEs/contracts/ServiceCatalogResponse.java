package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.ServiceCatalog;
import com.cenfotec.dondeEs.pojo.ServiceCatalogPOJO;

public class ServiceCatalogResponse extends BaseResponse {

	List<ServiceCatalogPOJO> serviceCatalogList;

	public List<ServiceCatalogPOJO> getServiceCatalogList() {
		return serviceCatalogList;
	}

	public void setServiceCatalogList(List<ServiceCatalogPOJO> serviceCatalogList) {
		this.serviceCatalogList = serviceCatalogList;
	}
	
	
}
