package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.ServiceResponse;
import com.cenfotec.dondeEs.ejb.Service;
import com.cenfotec.dondeEs.services.ServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/service")
public class ServiceController {

	@Autowired private ServiceInterface serviceInterface;
	
	@RequestMapping(value ="/getAllService", method = RequestMethod.GET)
	public ServiceResponse getAllService(){
		ServiceResponse response = new ServiceResponse();
		response.setServiceLists(serviceInterface.getAll());
		return response;
	}
	
	@RequestMapping(value ="/createService", method = RequestMethod.POST)
	public ServiceResponse createService(@RequestBody Service service){
		ServiceResponse response = new ServiceResponse();
		
		Boolean state = serviceInterface.saveService(service);
	
		if(state){
			response.setCode(200);
			response.setCodeMessage("Succesfull");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		return response;
	}
	
	@RequestMapping(value ="/getService/{serviceId}", method = RequestMethod.GET)
	public ServiceResponse getService(@PathVariable("serviceId") int serviceId){
		ServiceResponse response = new ServiceResponse();
		response.setService(serviceInterface.getService(serviceId));
		return response;
	}

	/**
	 * @author Ernesto Mendez A.
	 * @param catalogId id del catalogo al cual el servicio pertenece
	 * @return lista de servicios con el catalogo especificado
	 * @version 1.0
	 */
	@RequestMapping(value ="/getServicesByCatalog/{catalogId}", method = RequestMethod.GET)
	public ServiceResponse getServicesByCatalog(@PathVariable("catalogId") int catalogId){
		ServiceResponse response = new ServiceResponse();
		response.setServiceLists(serviceInterface.getByCatalog(catalogId));
		return response;
	}

	@RequestMapping(value ="/getServiceByProvider/{userId}", method = RequestMethod.GET)
	public ServiceResponse getServiceByProvider(@PathVariable("userId") int userId){
		ServiceResponse response = new ServiceResponse();
		response.setServiceLists(serviceInterface.getByProvider(userId));
		
		return response;
	}
	
	/**
	 * @author Juan Carlos Sánchez G.
	 * @param userId id del usuario, serviceCatalogId id del tipo de servicio
	 * @return Lista de servicios por usuario de un tipo especifico
	 * @version 1.0
	 */
	@RequestMapping(value ="/getAllServiceByUserAndServiceCatalog/{userId}/{serviceCatalogId}", method = RequestMethod.GET)
	public ServiceResponse getAllServiceByUserAndServiceCatalog(@PathVariable("userId") int userId, @PathVariable("serviceCatalogId") int serviceCatalogId){
		ServiceResponse response = new ServiceResponse();
		response.setServiceLists(serviceInterface.getAllServiceByUserAndServiceCatalog(userId,serviceCatalogId));
		return response;
	}
	
	@RequestMapping(value ="/getServiceCatalogIdByProvider/{userId}", method = RequestMethod.GET)
	public ServiceResponse getServiceCatalogIdByProvider(@PathVariable("userId") int userId){
		ServiceResponse response = new ServiceResponse();
		response.setServiceLists(serviceInterface.getServiceCatalogIdByProvider(userId));
		
		return response;
	}
	
	/**
	 * @author Juan Carlos Sánchez G.
	 * @param service servicio con los datos modificados, serviceId id del servicio a modificar
	 * @return booleano que indica si se modifico el servicio
	 * @version 1.0
	 */
	
	@RequestMapping(value ="/updateService", method = RequestMethod.PUT)
	public ServiceResponse updateService(@RequestBody Service service){
		ServiceResponse response = new ServiceResponse();
		
		Service nservice = new Service();
		nservice.setServiceId(service.getServiceId());
		nservice.setName(service.getName());
		nservice.setDescription(service.getDescription());
		nservice.setState(service.getState());
		nservice.setServiceCatalog(service.getServiceCatalog());
		nservice.setUser(service.getUser());
		
		Boolean state = serviceInterface.saveService(nservice);
		
		if(state){
			response.setCode(200);
			response.setErrorMessage("update succesfully");
		}
		else{
			response.setCode(409);
			response.setErrorMessage("update conflict");
		}
		
		return response;
	}

}
