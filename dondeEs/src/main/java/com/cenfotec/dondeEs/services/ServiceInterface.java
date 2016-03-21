package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
public interface ServiceInterface {
	
	Boolean saveService(Service service);
	List<ServicePOJO> getAll();
	ServicePOJO getService(int idEvent);
	ServicePOJO getServiceById(int idService);
	List<ServicePOJO> getByProvider(int idUser);
	
	/**
	 * @author Ernesto Mendez A.
	 * @param catalogId id del catalogo
	 * @return lista de serviciosPOJO
	 * @version 1.0
	 */
	List<ServicePOJO> getByCatalog(int catalogId);
}
