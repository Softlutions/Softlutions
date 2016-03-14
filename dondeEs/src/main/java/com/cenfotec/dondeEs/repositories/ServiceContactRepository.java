package com.cenfotec.dondeEs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.ServiceContact;;

public interface ServiceContactRepository extends CrudRepository<ServiceContact, Integer> {
	List<ServiceContact> findAll();

	ServiceContact getByServiceContractId(int id);

	@Query("SELECT sc FROM ServiceContact sc JOIN sc.event e WHERE e.eventId = ?1")
	List<ServiceContact> findServiceContactByEventId(int eventId);

	/**
	 * 
	 * @param eventId id del evento
	 * @param serviceId id del servicio
	 * @return retorna un contactService que coincida con los ids
	 */
	ServiceContact getByServiceServiceIdAndEventEventId(int eventId, int serviceId);
	ServiceContact getByServiceServiceIdAndEventAuctionsAuctionServicesAuctionServicesId(int eventId, int serviceId);
}
